package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkImageMemoryBarrier(
        val srcAccessMask: List<VkAccessFlagBits>,
        val dstAccessMask: List<VkAccessFlagBits>,
        val oldLayout: VkImageLayout,
        val newLayout: VkImageLayout,
        val srcQueueFamilyIndex: Int,
        val dstQueueFamilyIndex: Int,
        val image: VkImage,
        val subresourceRange: VkImageSubresourceRange)
