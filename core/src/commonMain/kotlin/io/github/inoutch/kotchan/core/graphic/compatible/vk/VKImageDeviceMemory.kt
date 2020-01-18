package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.utility.MappedMemory

class VKImageDeviceMemory(
        val image: VKImage,
        val deviceMemory: VKDeviceMemory  // Must be instance where no one is managing disposing
) : Disposer() {
    init {
        add(deviceMemory)
    }

    fun mapMemory(offset: Long = 0, flags: List<VkPipelineStageFlagBits> = emptyList()): MappedMemory {
        return deviceMemory.mapMemory(offset, image.memoryRequirements.size, flags)
    }
}
