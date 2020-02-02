package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.utility.MappedMemory

class VKBufferDeviceMemory(
    val buffer: VKBuffer,
    val deviceMemory: VKDeviceMemory
) : Disposer() {
    init {
        add(deviceMemory)
    }

    fun mapMemory(offset: Long = 0, flags: List<VkPipelineStageFlagBits> = emptyList()): MappedMemory {
        return deviceMemory.mapMemory(offset, buffer.bufferMemoryRequirements.size - offset, flags)
    }
}
