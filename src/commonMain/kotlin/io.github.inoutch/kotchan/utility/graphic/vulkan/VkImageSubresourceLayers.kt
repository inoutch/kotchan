package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkImageSubresourceLayers(
        val aspectMask: List<VkImageAspectFlagBits>,
        val mipLevel: Int,
        val baseArrayLayer: Int,
        val layerCount: Int)
