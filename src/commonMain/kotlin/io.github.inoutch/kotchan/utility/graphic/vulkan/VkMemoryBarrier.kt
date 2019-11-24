package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkMemoryBarrier(
    val srcAccessFlags: List<VkAccessFlagBits>,
    val dstAccessFlags: List<VkAccessFlagBits>
)
