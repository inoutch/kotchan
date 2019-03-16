package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkFormatProperties(
        val linearTilingFeatures: List<VkFormatFeatureFlagBits>,
        val optimalTilingFeatures: List<VkFormatFeatureFlagBits>,
        val bufferFeatures: List<VkFormatFeatureFlagBits>)

expect fun vkGetPhysicalDeviceFormatProperties(
        physicalDevice: VkPhysicalDevice,
        format: VkFormat): VkFormatProperties
