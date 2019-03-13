package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.error.NoSuchFileError
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.batch.BatchPolygonBundle
import io.github.inoutch.kotchan.core.graphic.shader.Shader
import io.github.inoutch.kotchan.core.graphic.shader.unform.*
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.graphic.Image
import io.github.inoutch.kotchan.utility.graphic.gl.GL
import io.github.inoutch.kotchan.utility.graphic.gl.GLAttribLocation
import io.github.inoutch.kotchan.utility.graphic.gl.GLShader
import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.Helper
import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.PointRect
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class Api(private val vk: VK?, private val gl: GL?) {

    val allocateBuffer = checkSupportGraphics({
        { size: Int ->
            val device = it.device
            val bufferCreateInfo = VkBufferCreateInfo(
                    0,
                    4L * size,
                    listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT),
                    VkSharingMode.VK_SHARING_MODE_EXCLUSIVE,
                    null)
            val buffer = vkCreateBuffer(device, bufferCreateInfo)

            val memoryTypes = listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                    VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)
            val requirements = vkGetBufferMemoryRequirements(device, buffer)
            val memoryAllocateInfo = VkMemoryAllocateInfo(
                    requirements.size,
                    it.getMemoryTypeIndex(requirements.memoryTypeBits, memoryTypes))
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
                    offset.toLong(),
                    vkBuffer.allocationSize,
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
            vkCmdBindVertexBuffers(
                    it.currentCommandBuffer,
                    0,
                    listOf(posBuffer.buffer, colBuffer.buffer, texBuffer.buffer),
                    listOf(0, 0, 0))

            vkCmdDraw(it.currentCommandBuffer, batchBufferBundle.size, 1, 0, 0)
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
            // create uniform buffer objects
            val descriptorSetLayout = createDescriptorSetLayout(it, createInfo.shaderProgram.uniforms)

            val descriptorSets = it.createDescriptorSets(
                    it.device, it.descriptorPool, it.commandBuffers.size, descriptorSetLayout)

            createInfo.shaderProgram.uniforms.forEach { uniform ->
                val uniformBuffer = VKUniformBuffer(it, uniform.binding, uniform.size)
                uniformBuffer.buffers.forEachIndexed { index, buffer ->
                    val bufferInfo = VkDescriptorBufferInfo(buffer.buffer, 0, uniform.size)
                    val descriptorWrite = VkWriteDescriptorSet(
                            descriptorSets[index],
                            uniform.binding,
                            1,
                            VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER,
                            listOf(), listOf(bufferInfo), listOf())
                    vkUpdateDescriptorSets(it.device, listOf(descriptorWrite), listOf())
                }
                uniform.vkUniform = uniformBuffer
            }

            val pipelineLayout = createPipelineLayout(it, listOf(descriptorSetLayout))

            val pipeline = Helper.createGraphicsPipeline(
                    it.device, it.renderPass, pipelineLayout, shader.vert, shader.frag)
            return@pipeline GraphicsPipeline(createInfo,
                    GraphicsPipeline.VKBundle(
                            pipeline,
                            descriptorSetLayout,
                            descriptorSets,
                            pipelineLayout))
        }
    }, {
        pipeline@{ createInfo: GraphicsPipeline.CreateInfo ->
            createInfo.shaderProgram.uniforms.forEach { uniform ->
                uniform.glUniform = it.getUniform(
                        createInfo.shaderProgram.shader.glShader?.id ?: 0,
                        uniform.uniformName)
            }
            return@pipeline GraphicsPipeline(createInfo)
        }
    })

    val bindGraphicsPipeline = checkSupportGraphics({
        pipeline@{ pipeline: GraphicsPipeline ->
            val vkPipeline = pipeline.vkBundle?.pipeline ?: return@pipeline
            vkCmdBindPipeline(
                    it.currentCommandBuffer,
                    VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                    vkPipeline)
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
        { filepath: String ->
            val data = instance.file.readBytes(filepath) ?: throw NoSuchFileError(filepath)
            val tex = VKTexture(it, Image.load(data))
            Texture(tex, null)
        }
    }, {
        { filepath: String ->
            val tex = it.loadTexture(filepath) ?: throw NoSuchFileError(filepath)
            Texture(null, tex)
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

    private fun createDescriptorSetLayout(vk: VK, uniforms: List<Uniform>): VkDescriptorSetLayout {
        val createInfo = VkDescriptorSetLayoutCreateInfo(0, uniforms.map {
            VkDescriptorSetLayoutBinding(
                    it.binding,
                    VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER,
                    1,
                    listOf(VkShaderStageFlagBits.VK_SHADER_STAGE_ALL_GRAPHICS),
                    null)
        })

        return vkCreateDescriptorSetLayout(vk.device, createInfo)
    }

    private fun createPipelineLayout(vk: VK, descriptorSets: List<VkDescriptorSetLayout>): VkPipelineLayout {
        val pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo(0, descriptorSets, listOf())
        return vkCreatePipelineLayout(vk.device, pipelineLayoutCreateInfo)
    }
}
