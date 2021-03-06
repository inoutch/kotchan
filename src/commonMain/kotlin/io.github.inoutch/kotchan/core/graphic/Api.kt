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

    data class TextureBundle(val vkTexture: VKTexture? = null,
                             val glTexture: GLTexture? = null)

    private var sharedTexture: Texture? = null

    private var currentPipeline: GraphicsPipeline? = null

    val begin = checkSupportGraphics({
        {
            it.begin()
        }
    }, {
        {
            it.enableBlend()
        }
    })

    val end = checkSupportGraphics({
        { it.end() }
    }, {
        {
            it.useProgram(0)
            it.bindVBO(0)
            it.disableVertexPointer(GLAttribLocation.ATTRIBUTE_COLOR)
            it.disableVertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD)
            it.disableVertexPointer(GLAttribLocation.ATTRIBUTE_POSITION)
        }
    })

    val allocateVertexBuffer = checkSupportGraphics({ vk ->
        { size: Int ->
            val memTypes = listOf(
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)
            val vertexBuffer = VKBufferMemory(
                    vk,
                    size.toLong() * FLOAT_SIZE,
                    listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT), memTypes)
            VertexBuffer(vertexBuffer, null)
        }
    }, {
        { size: Int ->
            VertexBuffer(null, it.createVBO(size))
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
            it.updateVBO(glBuffer, offset, vertices)
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
            it.bindVBO(batchBufferBundle.positionBuffer.vertexBuffer.glBuffer?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
            it.bindVBO(batchBufferBundle.texcoordBuffer.vertexBuffer.glBuffer?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
            it.bindVBO(batchBufferBundle.colorBuffer.vertexBuffer.glBuffer?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, 0, 0)

            it.drawTriangleArrays(0, batchBufferBundle.size)
        }
    })

    val createShader = checkSupportGraphics({
        { vert: ShaderProgram.ShaderSource, frag: ShaderProgram.ShaderSource ->
            val vertModule = vkCreateShaderModule(it.device, VkShaderModuleCreateInfo(0, vert.binary))
            val fragModule = vkCreateShaderModule(it.device, VkShaderModuleCreateInfo(0, frag.binary))
            Shader(VKShader(it, vertModule, fragModule), null)
        }
    }, {
        { vert: ShaderProgram.ShaderSource, frag: ShaderProgram.ShaderSource ->
            Shader(null, GLShader(it, it.compileShaderProgram(vert.text, frag.text)))
        }
    })

    val createGraphicsPipeline: (shaderProgram: ShaderProgram, config: GraphicsPipeline.Config) -> GraphicsPipeline = checkSupportGraphics({ vk ->
        pipeline@{ shaderProgram: ShaderProgram, config: GraphicsPipeline.Config ->
            val shader = shaderProgram.shader.vkShader ?: throw Error("no vulkan shader module")
            val uniforms = shaderProgram.descriptorSets
                    .filterIsInstance<Uniform>()
            val uniformBuffers = uniforms.map { VKUniformBuffer(vk, it.binding, it.size) }
            val samplers = shaderProgram.descriptorSets
                    .filterIsInstance<Sampler>()
            val descriptorSetLayout = createDescriptorSetLayout(vk, shaderProgram.descriptorSets)
            val descriptorSetProvider = DescriptorSetProvider(
                    vk, descriptorSetLayout, uniformBuffers, samplers.size)
            val samplerBuffers = samplers.map { VKSampler(descriptorSetProvider, it.binding) }

            val pipelineLayout = vk.createPipelineLayout(listOf(descriptorSetLayout))
            val pipeline = Helper.createGraphicsPipeline(
                    vk.device,
                    vk.renderPass,
                    pipelineLayout,
                    shader.vert, shader.frag,
                    config.depthTest,
                    convertVkCullMode(config.cullMode),
                    convertVkPolygonMode(config.polygonMode),
                    convertVkBlendFactor(config.srcBlendFactor),
                    convertVkBlendFactor(config.dstBlendFactor))

            return@pipeline GraphicsPipeline(shaderProgram, config,
                    VKPipeline(vk, pipeline, descriptorSetLayout, descriptorSetProvider, pipelineLayout,
                            uniformBuffers, samplerBuffers))
        }
    }, {
        pipeline@{ shaderProgram: ShaderProgram, config: GraphicsPipeline.Config ->
            val shaderId = shaderProgram.shader.glShader?.id ?: 0
            shaderProgram.descriptorSets.forEach { descriptor ->
                when (descriptor) {
                    is Uniform -> descriptor.glUniform = it.getUniform(shaderId, descriptor.descriptorName)
                    is Sampler -> descriptor.glSampler = it.getUniform(shaderId, descriptor.descriptorName)
                }
            }
            return@pipeline GraphicsPipeline(shaderProgram, config)
        }
    })

    val bindGraphicsPipeline = checkSupportGraphics({
        pipeline@{ pipeline: GraphicsPipeline ->
            val vkPipeline = pipeline.vkPipeline ?: return@pipeline

            vkCmdBindPipeline(
                    it.currentCommandBuffer,
                    VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                    vkPipeline.pipeline)

            val uniforms = pipeline.shaderProgram.descriptorSets.filterIsInstance<Uniform>()
            val samplers = pipeline.shaderProgram.descriptorSets.filterIsInstance<Sampler>()
            uniforms.forEachIndexed { index, uniform -> uniform.vkUniform = vkPipeline.uniforms[index] }
            samplers.forEachIndexed { index, sampler -> sampler.vkSampler = vkPipeline.samplers[index] }

            currentPipeline = pipeline
        }
    }, {
        pipeline@{ pipeline: GraphicsPipeline ->
            if (pipeline.config.depthTest) {
                it.enableDepth()
            } else {
                it.disableDepth()
            }
            it.useProgram(pipeline.shaderProgram.shader.glShader?.id ?: return@pipeline)
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

    val clearColor = checkSupportGraphics({
        { color: Vector4 ->
            vkCmdClearColorImage(
                    it.currentCommandBuffer,
                    it.swapchainRecreator.swapchainImages[it.currentImageIndex],
                    VkImageLayout.VK_IMAGE_LAYOUT_GENERAL,
                    color,
                    listOf(VkImageSubresourceRange(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT), 0, 1, 0, 1)))
        }
    }, {
        { color: Vector4 ->
            it.clearColor(color.x, color.y, color.z, color.w)
        }
    })

    val clearDepth = checkSupportGraphics({
        { depthColor: Float ->
            vkCmdClearDepthStencilImage(
                    it.currentCommandBuffer,
                    it.swapchainRecreator.depthResources[it.currentImageIndex].depthImage,
                    VkImageLayout.VK_IMAGE_LAYOUT_GENERAL,
                    VkClearDepthStencilValue(depthColor, 0),
                    listOf(VkImageSubresourceRange(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_DEPTH_BIT), 0, 1, 0, 1)))

        }
    }, {
        { depthColor: Float ->
            it.clearDepth(depthColor, depthColor, depthColor, depthColor)
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
            it.uniform4f(uniform.binding, value)
        }
    })

    val setUniformMatrix4 = checkSupportGraphics({
        { uniform: UniformMatrix4fv, value: Matrix4 ->
            uniform.vkUniform?.copy(value.flatten(), 0)
        }
    }, {
        { uniform: UniformMatrix4fv, value: Matrix4 ->
            it.uniformMatrix4fv(uniform.binding, 1, false, value)
        }
    })

    val setSampler = checkSupportGraphics({
        { sampler: Sampler, texture: Texture ->
            sampler.vkSampler?.setTexture(texture.vkTexture ?: throw IllegalStateException())
        }
    }, {
        { sampler: Sampler, texture: Texture ->
            texture.glTexture?.use()
            it.uniform1i(sampler.glSampler ?: 0, sampler.binding)
            it.activeTexture(sampler.binding)
        }
    })

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
                val colors = listOf(Vector4(1.0f))
                val size = Point(1, 1)
                callback(TextureBundle(null, it.createTexture(colors, size)))
                        .also { sharedTexture = it }
            }
        }
    })

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

    private fun convertVkBlendFactor(factor: BlendFactor) = when (factor) {
        BlendFactor.One -> VkBlendFactor.VK_BLEND_FACTOR_ONE
        BlendFactor.Zero -> VkBlendFactor.VK_BLEND_FACTOR_ZERO
        BlendFactor.SrcColor -> VkBlendFactor.VK_BLEND_FACTOR_SRC_COLOR
        BlendFactor.OneMinusSrcColor -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_SRC_COLOR
        BlendFactor.DstColor -> VkBlendFactor.VK_BLEND_FACTOR_DST_COLOR
        BlendFactor.OneMinusDstColor -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_DST_COLOR
        BlendFactor.SrcAlpha -> VkBlendFactor.VK_BLEND_FACTOR_SRC_ALPHA
        BlendFactor.OneMinusSrcAlpha -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA
        BlendFactor.DstAlpha -> VkBlendFactor.VK_BLEND_FACTOR_DST_ALPHA
        BlendFactor.OneMinusDstAlpha -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_DST_ALPHA
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
