package io.github.inoutch.kotchan.utility.graphic.vulkan

// not disposable because not created
expect class VkPhysicalDevice()

expect fun vkEnumeratePhysicalDevices(instance: VkInstance): List<VkPhysicalDevice>
