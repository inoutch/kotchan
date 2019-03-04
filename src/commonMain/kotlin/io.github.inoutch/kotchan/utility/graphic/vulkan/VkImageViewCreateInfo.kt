package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkImageViewCreateInfo(
        val flags: Int,
        val image: VkImage,
        val viewType: VkImageViewType,
        val format: VkFormat,
        val components: VkComponentMapping,
        val subresourceRange: VkImageSubresourceRange)
