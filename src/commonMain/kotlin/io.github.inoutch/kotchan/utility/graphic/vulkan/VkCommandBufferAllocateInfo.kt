package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkCommandBufferAllocateInfo(
        val commandPool: VkCommandPool,
        val level: Int,
        val commandBufferCount: Int)
