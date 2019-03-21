package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

fun org.lwjgl.vulkan.VkFormatProperties.toOrigin(): VkFormatProperties {
    return VkFormatProperties(
            VkFormatFeatureFlagBits.values().filter { it.value and linearTilingFeatures() != 0 },
            VkFormatFeatureFlagBits.values().filter { it.value and optimalTilingFeatures() != 0 },
            VkFormatFeatureFlagBits.values().filter { it.value and bufferFeatures() != 0 })
}

actual fun vkGetPhysicalDeviceFormatProperties(physicalDevice: VkPhysicalDevice, format: VkFormat) = memScoped {
    val native = add(org.lwjgl.vulkan.VkFormatProperties.calloc())

    VK10.vkGetPhysicalDeviceFormatProperties(physicalDevice.native, format.value, native)

    native.toOrigin()
}
