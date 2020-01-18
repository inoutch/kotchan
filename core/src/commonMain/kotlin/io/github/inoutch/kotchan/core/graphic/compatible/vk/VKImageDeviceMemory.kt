package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkDeviceMemory
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.MappedMemory

class VKImageDeviceMemory(
        val image: VKImage,
        deviceMemory: VkDeviceMemory
) : VKDeviceMemory(image.logicalDevice, deviceMemory) {
    fun mapMemory(offset: Long = 0, flags: List<VkPipelineStageFlagBits> = emptyList()): MappedMemory {
        return getProperty {
            vk.mapMemory(logicalDevice.device, deviceMemory, offset, image.memoryRequirements.size, flags, it).value
        }
    }
}
