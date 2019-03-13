package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.utility.graphic.vulkan.VK
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class VertexBuffer(
        vk: VK,
        vertices: FloatArray,
        memoryTypes: List<VkMemoryPropertyFlagBits> =
                listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)) : Disposable {

    val buffer: VkBuffer
    val memory: VkDeviceMemory
    val allocateSize: Long

    init {
        val bufferCreateInfo = VkBufferCreateInfo(
                0,
                4L * vertices.size,
                listOf(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT),
                VkSharingMode.VK_SHARING_MODE_EXCLUSIVE,
                null)
        buffer = vkCreateBuffer(vk.device, bufferCreateInfo)

        val requirements = vkGetBufferMemoryRequirements(vk.device, buffer)
        val memoryAllocateInfo = VkMemoryAllocateInfo(
                requirements.size,
                vk.getMemoryTypeIndex(requirements.memoryTypeBits, memoryTypes))
        memory = vkAllocateMemory(vk.device, memoryAllocateInfo)

        vkBindBufferMemory(vk.device, buffer, memory, 0)

        val mappedMemory = vkMapMemory(vk.device, memory, 0, memoryAllocateInfo.allocationSize, listOf())
        mappedMemory.copy(0, vertices.size.toLong(), vertices)
        mappedMemory.dispose()

        allocateSize = memoryAllocateInfo.allocationSize
    }

    override fun dispose() {
        memory.dispose()
        buffer.dispose()
    }
}
