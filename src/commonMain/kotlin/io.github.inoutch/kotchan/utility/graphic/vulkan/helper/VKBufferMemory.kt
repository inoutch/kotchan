package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.*

class VKBufferMemory(
        vk: VK,
        val size: Long,
        usage: List<VkBufferUsageFlagBits>,
        memoryTypes: List<VkMemoryPropertyFlagBits>) : Disposable {

    val buffer: VkBuffer
    val memory: VkDeviceMemory
    val allocateSize: Long

    init {
        val bufferCreateInfo = VkBufferCreateInfo(
                0,
                size,
                usage,
                VkSharingMode.VK_SHARING_MODE_EXCLUSIVE,
                null)
        buffer = vkCreateBuffer(vk.device, bufferCreateInfo)

        val requirements = vkGetBufferMemoryRequirements(vk.device, buffer)
        val memoryAllocateInfo = VkMemoryAllocateInfo(
                requirements.size,
                vk.getMemoryTypeIndex(requirements.memoryTypeBits, memoryTypes))
        memory = vkAllocateMemory(vk.device, memoryAllocateInfo)

        vkBindBufferMemory(vk.device, buffer, memory, 0)

        allocateSize = memoryAllocateInfo.allocationSize
    }

    override fun dispose() {
        memory.dispose()
        buffer.dispose()
    }
}
