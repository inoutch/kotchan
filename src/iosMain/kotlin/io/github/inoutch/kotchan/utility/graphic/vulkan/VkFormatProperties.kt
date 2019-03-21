package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun vulkan.VkFormatProperties.toOrigin(): VkFormatProperties {
    return VkFormatProperties(
            VkFormatFeatureFlagBits.values().filter { it.value.toUInt() and linearTilingFeatures != 0u },
            VkFormatFeatureFlagBits.values().filter { it.value.toUInt() and optimalTilingFeatures != 0u },
            VkFormatFeatureFlagBits.values().filter { it.value.toUInt() and bufferFeatures != 0u })
}

@ExperimentalUnsignedTypes
actual fun vkGetPhysicalDeviceFormatProperties(
        physicalDevice: VkPhysicalDevice,
        format: VkFormat): VkFormatProperties = memScoped {
    val native = alloc<vulkan.VkFormatProperties>()

    vulkan.vkGetPhysicalDeviceFormatProperties(physicalDevice.native, format.value.toUInt(), native.ptr)

    native.toOrigin()
}
