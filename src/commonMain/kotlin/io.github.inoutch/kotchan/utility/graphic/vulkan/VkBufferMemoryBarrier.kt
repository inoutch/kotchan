package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkBufferMemoryBarrier(
    val srcAccessMask: List<VkAccessFlagBits>,
    val dstAccessMask: List<VkAccessFlagBits>,
    val srcQueueFamilyIndex: Int,
    val dstQueueFamilyIndex: Int,
    val buffer: VkBuffer,
    val offset: Long,
    val size: Long
)
