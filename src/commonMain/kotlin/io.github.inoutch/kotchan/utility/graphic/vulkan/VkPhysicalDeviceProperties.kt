package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPhysicalDeviceProperties(
        val apiVersion: Int,
        val driverVersion: Int,
        val vendorId: Int,
        val deviceId: Int,
        val deviceType: VkPhysicalDeviceType,
        val deviceName: String,
        val pipelineCacheUUID: List<Int>,
        val limits: VkPhysicalDeviceLimits,
        val sparseProperties: VkPhysicalDeviceSparseProperties)

expect fun vkGetPhysicalDeviceProperties(physicalDevice: VkPhysicalDevice): VkPhysicalDeviceProperties
