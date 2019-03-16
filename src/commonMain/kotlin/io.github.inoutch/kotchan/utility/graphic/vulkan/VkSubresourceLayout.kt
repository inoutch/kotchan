package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSubresourceLayout(
        val offset: Long,
        val size: Long,
        val rowPitch: Long,
        val arrayPitch: Long,
        val depthPitch: Long)

expect fun vkGetImageSubresourceLayout(
        device: VkDevice, image: VkImage, subresource: VkImageSubresource): VkSubresourceLayout
