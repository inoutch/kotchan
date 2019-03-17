package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.batch.BatchPolygonBundle
import io.github.inoutch.kotchan.core.graphic.shader.Descriptor
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
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.Helper
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKSampler
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKTexture
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKUniformBuffer
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError
import io.github.inoutch.kotchan.utility.type.*

class Api(private val vk: VK?, private val gl: GL?) {

    private var sharedEmptyTexture: Texture? = null

    val allocateBuffer = checkSupportGraphics({ vk ->
        { size: Int ->
            val device = vk.device
            val bufferCreateInfo = VkBufferCreateInfo(
                    0, 4L * size,
                    listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT),
                    VkSharingMode.VK_SHARING_MODE_EXCLUSIVE,
                    null)

            val buffer = vkCreateBuffer(device, bufferCreateInfo)

            val memoryTypes = listOf(
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)
            val requirements = vkGetBufferMemoryRequirements(device, buffer)
            val memoryAllocateInfo = VkMemoryAllocateInfo(
                    requirements.size,
                    vk.findMemoryTypeIndex(requirements.memoryTypeBits, memoryTypes))
            val memory = vkAllocateMemory(device, memoryAllocateInfo)

            vkBindBufferMemory(device, buffer, memory, 0)
            Buffer(Buffer.VKBundle(buffer, memory, memoryAllocateInfo.allocationSize), null)
        }
    }, {
        { size: Int ->
            Buffer(null, Buffer.GLBundle(it.createVBO(size)))
        }
    })

    val copyToBuffer = checkSupportGraphics({
        copy@{ buffer: Buffer, vertices: FloatArray, offset: Int ->
            val vkBuffer = buffer.vkBuffer ?: return@copy
            val mappedMemory = vkMapMemory(
                    it.device,
                    vkBuffer.memory,
                    offset.toLong() * FLOAT_SIZE,
                    vertices.size.toLong(),
                    listOf())
            mappedMemory.copy(0, vertices.size.toLong(), vertices)
            mappedMemory.dispose() // unmap
        }
    }, {
        copy@{ buffer: Buffer, vertices: FloatArray, offset: Int ->
            val glBuffer = buffer.glBuffer ?: return@copy
            it.updateVBO(glBuffer.vbo, offset, vertices)
        }
    })

    val drawTriangles = checkSupportGraphics({
        draw@{ batchBufferBundle: BatchPolygonBundle ->
            val posBuffer = batchBufferBundle.positionBuffer.buffer.vkBuffer ?: return@draw
            val colBuffer = batchBufferBundle.colorBuffer.buffer.vkBuffer ?: return@draw
            val texBuffer = batchBufferBundle.texcoordBuffer.buffer.vkBuffer ?: return@draw

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

            vkCmdBeginRenderPass(it.currentCommandBuffer, renderPassBeginInfo, VkSubpassContents.VK_SUBPASS_CONTENTS_INLINE)

            vkCmdDraw(it.currentCommandBuffer, batchBufferBundle.size, 1, 0, 0)

            vkCmdEndRenderPass(it.currentCommandBuffer)
        }
    }, {
        { batchBufferBundle: BatchPolygonBundle ->
            it.bindVBO(batchBufferBundle.positionBuffer.buffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
            it.bindVBO(batchBufferBundle.texcoordBuffer.buffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
            it.bindVBO(batchBufferBundle.colorBuffer.buffer.glBuffer?.vbo?.id ?: 0)
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

    val createGraphicsPipeline = checkSupportGraphics({
        pipeline@{ createInfo: GraphicsPipeline.CreateInfo ->
            val shader = createInfo.shaderProgram.shader.vkShader ?: throw Error("no vulkan shader module")

            val descriptorSetLayout = createDescriptorSetLayout(it, createInfo.shaderProgram.descriptors)
            val descriptorPool = createDescriptorPool(it, createInfo.shaderProgram.descriptors)
            val descriptorSets = it.createDescriptorSets(
                    it.device, descriptorPool, it.commandBuffers.size, descriptorSetLayout)

            // set native data to each descriptors
            createInfo.shaderProgram.descriptors.forEach { descriptor ->
                when (descriptor) {
                    is Uniform -> {
                        val uniformBuffer = VKUniformBuffer(it, descriptor.binding, descriptor.size)
                        uniformBuffer.buffers.forEachIndexed { index, buffer ->
                            val bufferInfo = VkDescriptorBufferInfo(buffer.buffer, 0, descriptor.size)
                            val descriptorWrite = VkWriteDescriptorSet(
                                    descriptorSets[index],
                                    descriptor.binding,
                                    0,
                                    VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER,
                                    listOf(), listOf(bufferInfo), listOf())
                            vkUpdateDescriptorSets(it.device, listOf(descriptorWrite), listOf())
                        }
                        descriptor.vkUniform = uniformBuffer
                    }
                    is Sampler -> {
                        descriptor.vkSampler = VKSampler(descriptorSets, descriptor.binding)
                    }
                    else -> throw Error("unsupported descriptor type")
                }
            }

            val pipelineLayout = createPipelineLayout(it, listOf(descriptorSetLayout))
            val pipeline = Helper.createGraphicsPipeline(
                    it.device, it.renderPass, pipelineLayout, shader.vert, shader.frag)

            return@pipeline GraphicsPipeline(createInfo,
                    GraphicsPipeline.VKBundle(pipeline, descriptorSetLayout, descriptorSets, pipelineLayout))
        }
    }, {
        pipeline@{ createInfo: GraphicsPipeline.CreateInfo ->
            val shaderId = createInfo.shaderProgram.shader.glShader?.id ?: 0
            createInfo.shaderProgram.descriptors.forEach { descriptor ->
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
            val vkPipeline = pipeline.vkBundle ?: return@pipeline

            vkCmdBindPipeline(
                    it.currentCommandBuffer,
                    VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                    vkPipeline.pipeline)

            vkCmdBindDescriptorSets(
                    it.currentCommandBuffer,
                    VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                    vkPipeline.pipelineLayout,
                    0,
                    listOf(vkPipeline.descriptorSets[it.currentImageIndex]),
                    listOf())
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

    val loadTexture = checkSupportGraphics({
        loadTexture@{ filepath: String ->
            val data = instance.file.readBytes(filepath) ?: return@loadTexture null
            val tex = VKTexture(it, Image.load(data))
            Texture(tex, null)
        }
    }, {
        loadTexture@{ filepath: String ->
            val tex = it.loadTexture(filepath) ?: return@loadTexture null
            Texture(null, tex)
        }
    })

    fun loadTextureFromResource(filepath: String) =
            loadTexture(instance.file.getResourcePathWithError(filepath))

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
            sampler.vkSampler?.setTexture(it, texture)
        }
    }, {
        { sampler: Sampler, texture: Texture ->
            it.uniform1i(sampler.glSampler ?: 0, sampler.binding)
            it.activeTexture(sampler.binding)
            it.useTexture(texture.glTexture)
        }
    })

    val emptyTexture = checkSupportGraphics({
        {
            sharedEmptyTexture.getOrCreate {
                Texture(VKTexture(it, Image(byteArrayOf(-1, -1, -1, -1), Point(1, 1))))
                        .also { sharedEmptyTexture = it }
            }
        }
    }, {
        {
            sharedEmptyTexture.getOrCreate {
                Texture(null, GLTexture.empty)
                        .also { sharedEmptyTexture = it }
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

    private fun createDescriptorSetLayout(vk: VK, descriptors: List<Descriptor>): VkDescriptorSetLayout {
        val createInfo = VkDescriptorSetLayoutCreateInfo(0, descriptors.map {
            val type = getDescriptorType(it)
            val shaderStageFlag = if (type == VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER) {
                VkShaderStageFlagBits.VK_SHADER_STAGE_FRAGMENT_BIT
            } else {
                VkShaderStageFlagBits.VK_SHADER_STAGE_ALL_GRAPHICS
            }
            VkDescriptorSetLayoutBinding(it.binding, type, 1, listOf(shaderStageFlag), null)
        })

        return vkCreateDescriptorSetLayout(vk.device, createInfo)
    }

    private fun getDescriptorType(descriptor: Descriptor): VkDescriptorType {
        if (descriptor is Uniform) {
            return VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER
        } else if (descriptor is Sampler) {
            return VkDescriptorType.VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER
        }
        throw Error("unsupported descriptor type")
    }

    private fun createPipelineLayout(vk: VK, descriptorSets: List<VkDescriptorSetLayout>): VkPipelineLayout {
        val pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo(0, descriptorSets, listOf())
        return vkCreatePipelineLayout(vk.device, pipelineLayoutCreateInfo)
    }

    private fun createDescriptorPoolSize(vk: VK, descriptorType: VkDescriptorType): VkDescriptorPoolSize {
        return VkDescriptorPoolSize(descriptorType, vk.commandBuffers.size)
    }

    private fun createDescriptorPool(vk: VK, descriptors: List<Descriptor>): VkDescriptorPool {
        val sizes = descriptors.map { createDescriptorPoolSize(vk, getDescriptorType(it)) }
        return vkCreateDescriptorPool(vk.device, VkDescriptorPoolCreateInfo(listOf(), vk.commandBuffers.size, sizes))
    }
}
