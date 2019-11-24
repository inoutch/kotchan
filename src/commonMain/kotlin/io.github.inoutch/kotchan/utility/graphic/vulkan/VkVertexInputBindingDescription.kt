package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkVertexInputBindingDescription(
    val binding: Int,
    val stride: Int,
    val inputRate: VkVertexInputRate
)
