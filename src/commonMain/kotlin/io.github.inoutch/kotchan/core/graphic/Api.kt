package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.batch.BatchPolygonBundle
import io.github.inoutch.kotchan.core.graphic.shader.DescriptorSet
import io.github.inoutch.kotchan.core.graphic.shader.Sampler
import io.github.inoutch.kotchan.core.graphic.shader.Shader
import io.github.inoutch.kotchan.core.graphic.shader.unform.*
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.extension.getOrCreate
import io.github.inoutch.kotchan.utility.graphic.Image
import io.github.inoutch.kotchan.utility.graphic.gl.GL
import io.github.inoutch.kotchan.utility.graphic.gl.GLAttribLocation
import io.github.inoutch.kotchan.utility.graphic.gl.GLShader
import io.github.inoutch.kotchan.utility.graphic.gl.GLTexture
import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.*
import io.github.inoutch.kotchan.utility.type.*

class Api(private val vk: VK?, private val gl: GL?) {

    private var sharedTexture: Texture? = null

    private var currentPipeline: GraphicsPipeline? = null

    val allocateVertexBuffer = checkSupportGraphics({ vk ->
        { size: Int ->
            val memTypes = listOf(
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)
            val vertexBuffer = VKBufferMemory(
                    vk, size.toLong(), listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT), memTypes)
            VertexBuffer(vertexBuffer, null)
        }
    }, {
        { size: Int ->
            VertexBuffer(null, VertexBuffer.GLBundle(it.createVBO(size)))
        }
    })

    val copyToBuffer = checkSupportGraphics({
        copy@{ vertexBuffer: VertexBuffer, vertices: FloatArray, offset: Int ->
            val vkBuffer = vertexBuffer.vkBuffer ?: return@copy
            val mappedMemory = vkMapMemory(
                    it.device,
                    vkBuffer.memory,
                    offset.toLong() * FLOAT_SIZE,
                    vertices.size.toLong() * FLOAT_SIZE,
                    listOf())
            mappedMemory.copy(0, vertices.size.toLong(), vertices)
            mappedMemory.dispose() // unmap
        }
    }, {
        copy@{ vertexBuffer: VertexBuffer, vertices: FloatArray, offset: Int ->
            val glBuffer = vertexBuffer.glBuffer ?: return@copy
            it.updateVBO(glBuffer.vbo, offset, vertices)
        }
    })

    val drawTriangles = checkSupportGraphics({
        draw@{ batchBufferBundle: BatchPolygonBundle ->
            val currentPipeline = this.currentPipeline ?: throw Error("must bind graphic pipeline")
            val vkPipeline = currentPipeline.vkPipeline ?: return@draw
            val posBuffer = batchBufferBundle.positionBuffer.vertexBuffer.vkBuffer ?: return@draw
            val colBuffer = batchBufferBundle.colorBuffer.vertexBuffer.vkBuffer ?: return@draw
            val texBuffer = batchBufferBundle.texcoordBuffer.vertexBuffer.vkBuffer ?: return@draw
            val descriptorSet = vkPipeline.descriptorSetProvider.currentDescriptorSet

            val renderPassBeginInfo = VkRenderPassBeginInfo(
                    it.renderPass,
                    it.currentFrameBuffer,
                    VkRect2D(Point.ZERO, it.swapchainRecreator.extent),
                    listOf(VkClearValue(Vector4(0.5f, 0.5f, 0.5f, 1.0f)),
                            VkClearValue(VkClearDepthStencilValue(1.0f, 0))))

            vkCmdBindVertexBuffers(
                    it.currentCommandBuffer,
                    0,
                    listOf(posBuffer.buffer, colBuffer.buffer, texBuffer.buffer),
                    listOf(0, 0, 0))

            vkCmdBindDescriptorSets(
                    it.currentCommandBuffer,
                    VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                    vkPipeline.pipelineLayout,
                    0,
                    listOf(descriptorSet),
                    listOf())

            vkCmdBeginRenderPass(it.currentCommandBuffer, renderPassBeginInfo, VkSubpassContents.VK_SUBPASS_CONTENTS_INLINE)

            vkCmdDraw(it.currentCommandBuffer, batchBufferBundle.size, 1, 0, 0)

            vkCmdEndRenderPass(it.currentCommandBuffer)
        }
    }, {
        { batchBufferBundle: BatchPolygonBundle ->
            it.bindVBO(batchBufferBundle.positionBuffer.vertexBuffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
            it.bindVBO(batchBufferBundle.texcoordBuffer.vertexBuffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
            it.bindVBO(batchBufferBundle.colorBuffer.vertexBuffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, 0, 0)

            it.drawTriangleArrays(0, batchBufferBundle.size)
        }
    })

    val createShader = checkSupportGraphics({
        { vert: ShaderProgram.ShaderSource, frag: ShaderProgram.ShaderSource ->
            val vertModule = vkCreateShaderModule(it.device, VkShaderModuleCreateInfo(0, vert.binary))
            val fragModule = vkCreateShaderModule(it.device, VkShaderModuleCreateInfo(0, frag.binary))
            Shader(Shader.VKBundle(vertModule, fragModule), null)
        }
    }, {
        { vert: ShaderProgram.ShaderSource, frag: ShaderProgram.ShaderSource ->
            Shader(null, GLShader(it.compileShaderProgram(vert.text, frag.text)))
        }
    })

    val createGraphicsPipeline = checkSupportGraphics({ vk ->
        pipeline@{ createInfo: GraphicsPipeline.CreateInfo ->
            val shader = createInfo.shaderProgram.shader.vkShader ?: throw Error("no vulkan shader module")
            val uniforms = createInfo.shaderProgram.descriptorSets
                    .filterIsInstance<Uniform>()
                    .map { VKUniformBuffer(vk, it.binding, it.size) }
            val samplers = createInfo.shaderProgram.descriptorSets
                    .filterIsInstance<Sampler>()
            val descriptorSetLayout = createDescriptorSetLayout(vk, createInfo.shaderProgram.descriptorSets)
            val descriptorSetProvider = DescriptorSetProvider(vk, descriptorSetLayout, uniforms, samplers.size)

            createInfo.shaderProgram.descriptorSets.forEach { updateDescriptor(vk, it, descriptorSetProvider) }

            val pipelineLayout = vk.createPipelineLayout(listOf(descriptorSetLayout))
            val pipeline = Helper.createGraphicsPipeline(
                    vk.device,
                    vk.renderPass,
                    pipelineLayout,
                    shader.vert, shader.frag,
                    createInfo.depthTest,
                    convertVkCullMode(createInfo.cullMode),
                    convertVkPolygonMode(createInfo.polygonMode))

            return@pipeline GraphicsPipeline(createInfo,
                    VKPipeline(vk, pipeline, descriptorSetLayout, descriptorSetProvider, pipelineLayout))
        }
    }, {
        pipeline@{ createInfo: GraphicsPipeline.CreateInfo ->
            val shaderId = createInfo.shaderProgram.shader.glShader?.id ?: 0
            createInfo.shaderProgram.descriptorSets.forEach { descriptor ->
                if (descriptor is Uniform) {
                    descriptor.glUniform = it.getUniform(shaderId, descriptor.descriptorName)
                } else if (descriptor is Sampler) {
                    descriptor.glSampler = it.getUniform(shaderId, descriptor.descriptorName)
                }
            }
            return@pipeline GraphicsPipeline(createInfo)
        }
    })

    val bindGraphicsPipeline = checkSupportGraphics({
        pipeline@{ pipeline: GraphicsPipeline ->
            val vkPipeline = pipeline.vkPipeline ?: return@pipeline
            println(vkPipeline.hashCode())

            vkCmdBindPipeline(
                    it.currentCommandBuffer,
                    VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                    vkPipeline.pipeline)
            currentPipeline = pipeline
        }
    }, {
        pipeline@{ pipeline: GraphicsPipeline ->
            it.useProgram(pipeline.createInfo.shaderProgram.shader.glShader?.id ?: return@pipeline)
        }
    })

    val setViewport = checkSupportGraphics({
        { viewport: PointRect ->
            vkCmdSetViewport(it.currentCommandBuffer,
                    0,
                    listOf(VkViewport(
                            viewport.origin.x.toFloat(),
                            viewport.origin.y.toFloat() + viewport.size.y.toFloat(),
                            viewport.size.x.toFloat(),
                            -viewport.size.y.toFloat(), 0.0f, 1.0f)))

            // TODO: implementation scissor api
            vkCmdSetScissor(it.currentCommandBuffer, 0, listOf(VkRect2D(viewport.origin, viewport.size)))
        }
    }, {
        { viewport: PointRect ->
            it.viewPort(viewport.origin.x, viewport.origin.y, viewport.size.x, viewport.size.y)
        }
    })

    data class TextureBundle(val vkTexture: VKTexture? = null,
                             val glTexture: GLTexture? = null)

    val loadTexture = checkSupportGraphics({
        loadTexture@{ filepath: String ->
            val data = instance.file.readBytes(filepath) ?: return@loadTexture null
            val tex = VKTexture(it, Image.load(data))
            TextureBundle(tex, null)
        }
    }, {
        loadTexture@{ filepath: String ->
            val tex = it.loadTexture(filepath) ?: return@loadTexture null
            TextureBundle(null, tex)
        }
    })

    val setUniform1f = checkSupportGraphics({
        { uniform: Uniform1f, value: Float ->
            uniform.vkUniform?.copy(floatArrayOf(value), 0)
        }
    }, {
        { uniform: Uniform1f, value: Float ->
            uniform.glUniform?.let { l -> it.uniform1f(l, value) }
        }
    })

    val setUniform1i = checkSupportGraphics({
        { uniform: Uniform1i, value: Int -> uniform.vkUniform?.copy(intArrayOf(value), 0) }
    }, {
        { uniform: Uniform1i, value: Int ->
            uniform.glUniform?.let { l -> it.uniform1i(l, value) }
        }
    })

    val setUniform3f = checkSupportGraphics({
        { uniform: Uniform3f, value: Vector3 ->
            uniform.vkUniform?.copy(floatArrayOf(value.x, value.y, value.z), 0)
        }
    }, {
        { uniform: Uniform3f, value: Vector3 ->
            uniform.glUniform?.let { l -> it.uniform3f(l, value) }
        }
    })

    val setUniform4f = checkSupportGraphics({
        { uniform: Uniform4f, value: Vector4 ->
            uniform.vkUniform?.copy(floatArrayOf(value.x, value.y, value.z, value.w), 0)
        }
    }, {
        { uniform: Uniform4f, value: Vector4 ->
            uniform.glUniform?.let { l -> it.uniform4f(uniform.binding, value) }
        }
    })

    val setUniformMatrix4 = checkSupportGraphics({
        { uniform: UniformMatrix4fv, value: Matrix4 ->
            uniform.vkUniform?.copy(value.flatten(), 0)
        }
    }, {
        { uniform: UniformMatrix4fv, value: Matrix4 ->
            uniform.glUniform?.let { l -> it.uniformMatrix4fv(uniform.binding, 1, false, value) }
        }
    })

    val setSampler = checkSupportGraphics({
        { sampler: Sampler, texture: Texture ->
            sampler.vkSampler?.setTexture(texture.vkTexture ?: throw IllegalStateException())
        }
    }, {
        { sampler: Sampler, texture: Texture ->
            it.uniform1i(sampler.glSampler ?: 0, sampler.binding)
            it.activeTexture(sampler.binding)
            it.useTexture(texture.glTexture)
        }
    })

    val emptyTexture = checkSupportGraphics({
        { callback: (textureBundle: TextureBundle) -> Texture ->
            sharedTexture.getOrCreate {
                callback(TextureBundle(VKTexture(it, Image(byteArrayOf(-1, -1, -1, -1), Point(1, 1)))))
                        .also { sharedTexture = it }
            }
        }
    }, {
        { callback: (textureBundle: TextureBundle) -> Texture ->
            sharedTexture.getOrCreate {
                callback(TextureBundle(null, GLTexture.empty))
                        .also { sharedTexture = it }
            }
        }
    })

    private fun updateDescriptor(vk: VK, descriptorSet: DescriptorSet, descriptorSetProvider: DescriptorSetProvider) {
        when (descriptorSet) {
            is Uniform -> descriptorSet.vkUniform = VKUniformBuffer(vk, descriptorSet.binding, descriptorSet.size)
            is Sampler -> descriptorSet.vkSampler = VKSampler(descriptorSetProvider, descriptorSet.binding)
            else -> throw Error("unsupported descriptorSet type")
        }
    }

    private fun <T> checkSupportGraphics(vkScope: (vk: VK) -> T, glScope: (gl: GL) -> T): T {
        val vk = this.vk
        if (vk != null) {
            return vkScope(vk)
        }
        val gl = this.gl
        if (gl != null) {
            return glScope(gl)
        }
        throw Error("no graphics supported")
    }

    private fun createDescriptorSetLayout(vk: VK, descriptorSets: List<DescriptorSet>): VkDescriptorSetLayout {
        val createInfo = VkDescriptorSetLayoutCreateInfo(0, descriptorSets.map {
            val type = findDescriptorType(it)
            val shaderStageFlag = if (type == VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER) {
                VkShaderStageFlagBits.VK_SHADER_STAGE_FRAGMENT_BIT
            } else {
                VkShaderStageFlagBits.VK_SHADER_STAGE_ALL_GRAPHICS
            }
            VkDescriptorSetLayoutBinding(it.binding, type, 1, listOf(shaderStageFlag), null)
        })

        return vkCreateDescriptorSetLayout(vk.device, createInfo)
    }

    private fun findDescriptorType(descriptorSet: DescriptorSet): VkDescriptorType {
        if (descriptorSet is Uniform) {
            return VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER
        } else if (descriptorSet is Sampler) {
            return VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER
        }
        throw Error("unsupported descriptorSet type")
    }

    private fun convertVkCullMode(cullMode: CullMode) = when (cullMode) {
        CullMode.None -> VkCullMode.VK_CULL_MODE_NONE
        CullMode.Front -> VkCullMode.VK_CULL_MODE_FRONT_BIT
        CullMode.Back -> VkCullMode.VK_CULL_MODE_BACK_BIT
    }

    private fun convertVkPolygonMode(polygonMode: PolygonMode) = when (polygonMode) {
        PolygonMode.Fill -> VkPolygonMode.VK_POLYGON_MODE_FILL
        PolygonMode.Line -> VkPolygonMode.VK_POLYGON_MODE_LINE
        PolygonMode.Point -> VkPolygonMode.VK_POLYGON_MODE_POINT
    }
}
