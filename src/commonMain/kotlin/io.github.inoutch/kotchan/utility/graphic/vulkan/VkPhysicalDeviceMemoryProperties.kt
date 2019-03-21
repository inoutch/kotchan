package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPhysicalDeviceMemoryProperties(
        val memoryTypes: List<VkMemoryType>,
        val memoryHeaps: List<VkMemoryHeap>)

expect fun vkGetPhysicalDeviceMemoryProperties(physicalDevice: VkPhysicalDevice): VkPhysicalDeviceMemoryProperties
