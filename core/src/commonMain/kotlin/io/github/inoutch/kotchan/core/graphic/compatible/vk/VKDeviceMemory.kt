package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkDeviceMemory
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.MappedMemory

open class VKDeviceMemory(val logicalDevice: VKLogicalDevice, val deviceMemory: VkDeviceMemory) : Disposer() {
    init {
        add { vk.freeMemory(logicalDevice.device, deviceMemory) }
    }

    fun mapMemory(offset: Long, size: Long, flags: List<VkPipelineStageFlagBits> = emptyList()): MappedMemory {
        return getProperty { vk.mapMemory(logicalDevice.device, deviceMemory, offset, size, flags, it).value }
    }
}
