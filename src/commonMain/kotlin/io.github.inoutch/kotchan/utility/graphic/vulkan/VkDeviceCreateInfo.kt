package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDeviceCreateInfo(
        val flags: Int,
        val queueCreateInfos: List<VkDeviceQueueCreateInfo>,
        val enabledLayerNames: List<String>,
        val enabledExtensionNames: List<String>,
        val enabledFeatures: VkPhysicalDeviceFeatures?)
