package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDeviceQueueCreateInfo(
        val flags: Int,
        val queueFamilyIndex: Int,
        val queuePriorities: List<Float> = listOf(1.0f))
