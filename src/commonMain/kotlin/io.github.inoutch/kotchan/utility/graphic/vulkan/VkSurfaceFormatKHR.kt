package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSurfaceFormatKHR(
        val format: VkFormat, // pixel format
        val colorSpace: VkColorSpaceKHR)

expect fun vkGetPhysicalDeviceSurfaceFormatsKHR(
        physicalDevice: VkPhysicalDevice, surface: VkSurface): List<VkSurfaceFormatKHR>
