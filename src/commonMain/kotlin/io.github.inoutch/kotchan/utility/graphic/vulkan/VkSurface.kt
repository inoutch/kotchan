package io.github.inoutch.kotchan.utility.graphic.vulkan

expect class VkSurface

expect fun vkGetPhysicalDeviceSurfaceSupportKHR(
        physicalDevice: VkPhysicalDevice, queueFamilyIndex: Int, surface: VkSurface): Boolean

expect fun vkGetPhysicalDeviceSurfacePresentModesKHR(
        physicalDevice: VkPhysicalDevice, surface: VkSurface): List<VkPresentModeKHR>
