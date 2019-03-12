package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.graphic.batch.BatchPolygonBundle
import io.github.inoutch.kotchan.utility.graphic.gl.GL
import io.github.inoutch.kotchan.utility.graphic.gl.GLAttribLocation
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

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

            this.glBatchBuffer?.let { gl.deleteVBO(it.vbo.id) }
            this.glBatchBuffer = GLBatchBuffer(gl.createVBO(size))
        }
    })

    val drawTriangles = checkSupportGraphics({
        { batchBundle: BatchPolygonBundle ->

        }
    }, {
        { batchBufferBundle: BatchPolygonBundle ->
            it.bindVBO(batchBufferBundle.positionBuffer.buffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
            it.bindVBO(batchBufferBundle.texcoordBuffer.buffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
            it.bindVBO(batchBufferBundle.colorBuffer.buffer.glBuffer?.vbo?.id ?: 0)
            it.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, 0, 0)

            it.drawTriangleArrays(0, batchBufferBundle.getSize())
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
}
