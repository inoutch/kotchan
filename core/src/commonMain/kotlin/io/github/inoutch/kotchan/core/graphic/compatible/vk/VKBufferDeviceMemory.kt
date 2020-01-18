package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotlin.vulkan.api.VkDeviceMemory
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.utility.MappedMemory

class VKBufferDeviceMemory(
        val buffer: VKBuffer,
        deviceMemory: VkDeviceMemory
) : VKDeviceMemory(buffer.logicalDevice, deviceMemory) {

    fun mapMemory(offset: Long = 0, flags: List<VkPipelineStageFlagBits> = emptyList()): MappedMemory {
        return mapMemory(offset, buffer.bufferMemoryRequirements.size - offset, flags)
    }
}
