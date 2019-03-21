package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkInstanceCreateInfo(
        val flags: Int,
        val applicationInfo: VkApplicationInfo,
        val enabledLayerNames: List<String>,
        val enabledExtensionNames: List<String>)
