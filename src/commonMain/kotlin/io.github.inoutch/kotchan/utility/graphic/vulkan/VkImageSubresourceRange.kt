package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkImageSubresourceRange(
    val aspectMask: List<VkImageAspectFlagBits>,
    val baseMipLevel: Int,
    val levelCount: Int,
    val baseArrayLayer: Int,
    val layerCount: Int
)
