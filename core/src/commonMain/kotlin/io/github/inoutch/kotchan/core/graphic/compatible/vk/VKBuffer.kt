package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkBuffer
import io.github.inoutch.kotlin.vulkan.api.VkMemoryPropertyFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkMemoryRequirements
import io.github.inoutch.kotlin.vulkan.api.vk

class VKBuffer(
        val logicalDevice: VKLogicalDevice,
        val buffer: VkBuffer
) : Disposer() {
    val bufferMemoryRequirements by lazy {
        getProperty<VkMemoryRequirements> { vk.getBufferMemoryRequirements(logicalDevice.device, buffer, it) }
    }

    init {
        add { vk.destroyBuffer(logicalDevice.device, buffer) }
    }

    fun bindBufferMemory(memory: VKDeviceMemory, memoryOffset: Long) {
        vk.bindBufferMemory(logicalDevice.device, buffer, memory.deviceMemory, memoryOffset)
    }

    fun allocateBufferDeviceMemory(): VKBufferDeviceMemory {
        val memoryTypeIndex = checkNotNull(logicalDevice.physicalDevice.physicalDeviceMemoryProperties.findMemoryTypeIndex(
                bufferMemoryRequirements,
                listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT,
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT)
        )) { "Invalid memory type index" }
        val memory = logicalDevice.allocateDeviceMemory(bufferMemoryRequirements.size, memoryTypeIndex)
        add(memory)

        bindBufferMemory(memory, 0L)
        return VKBufferDeviceMemory(this, memory)
    }
}
