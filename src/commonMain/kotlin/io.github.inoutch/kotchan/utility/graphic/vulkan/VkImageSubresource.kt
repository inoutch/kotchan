package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkImageSubresource(
    val aspectMask: List<VkImageAspectFlagBits>,
    val mipLevel: Int,
    val arrayLayer: Int
)
