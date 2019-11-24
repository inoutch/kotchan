package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.type.Point

data class VkSwapchainCreateInfoKHR(
    val flags: Int,
    val surface: VkSurface,
    val minImageCount: Int,
    val imageFormat: VkFormat,
    val imageColorSpace: VkColorSpaceKHR,
    val imageExtent: Point,
    val imageArrayLayers: Int,
    val imageUsage: List<VkImageUsageFlagBits>,
    val imageSharingMode: VkSharingMode,
    val queueFamilyIndices: List<Int>?,
    val preTransform: VkSurfaceTransformFlagBitsKHR,
    val compositeAlpha: VkCompositeAlphaFlagBitsKHR,
    val presentMode: VkPresentModeKHR,
    val clipped: Boolean,
    val oldSwapchain: VkSwapchainKHR?
)
