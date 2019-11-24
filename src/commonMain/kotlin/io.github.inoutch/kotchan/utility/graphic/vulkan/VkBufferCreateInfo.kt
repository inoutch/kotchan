package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkBufferCreateInfo(
    val flags: Int,
    val size: Long,
    val usage: List<VkBufferUsageFlagBits>,
    val sharingMode: VkSharingMode,
    val queueFamilyIndices: List<Int>?
)
