package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkQueueFamilyProperties(
        val queueFlags: Int,
        val queueCount: Int,
        val timestampValidBits: Int,
        val minImageTransferGranularity: VkExtend3D)

expect fun vkGetPhysicalDeviceQueueFamilyProperties(
        physicalDevice: VkPhysicalDevice): List<VkQueueFamilyProperties>
