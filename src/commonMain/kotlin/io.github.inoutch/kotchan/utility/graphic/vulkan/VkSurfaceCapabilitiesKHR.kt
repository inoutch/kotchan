package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.type.Point

data class VkSurfaceCapabilitiesKHR(
    val minImageCount: Int,
    val maxImageCount: Int,
    val currentExtent: Point,
    val minImageExtent: Point,
    val maxImageExtent: Point,
    val maxImageArrayLayers: Int,
    val supportedTransforms: List<VkSurfaceTransformFlagBitsKHR>,
    val currentTransform: VkSurfaceTransformFlagBitsKHR,
    val supportedCompositeAlpha: List<VkCompositeAlphaFlagBitsKHR>,
    val supportedUsageFlagBits: List<VkImageUsageFlagBits>
)

expect fun vkGetPhysicalDeviceSurfaceCapabilitiesKHR(
    physicalDevice: VkPhysicalDevice,
    surface: VkSurface
): VkSurfaceCapabilitiesKHR
