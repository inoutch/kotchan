package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkVertexInputAttributeDescription(
    val location: Int,
    val binding: Int,
    val format: VkFormat,
    val offset: Int
)
