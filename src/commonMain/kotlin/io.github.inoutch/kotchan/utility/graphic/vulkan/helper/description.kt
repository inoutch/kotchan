package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.graphic.vulkan.*

fun create2DDescriptions(): List<VkVertexInputBindingDescription> {
    val inputRate = VkVertexInputRate.VK_VERTEX_INPUT_RATE_VERTEX
    return listOf(
            VkVertexInputBindingDescription(0, 3 * FLOAT_SIZE.toInt(), inputRate), // pos
            VkVertexInputBindingDescription(1, 4 * FLOAT_SIZE.toInt(), inputRate), // col
            VkVertexInputBindingDescription(2, 2 * FLOAT_SIZE.toInt(), inputRate)) // tex
}

fun create2DAttributes(): List<VkVertexInputAttributeDescription> {
    return listOf(
            VkVertexInputAttributeDescription(0, 0, VkFormat.VK_FORMAT_R32G32B32_SFLOAT, 0),
            VkVertexInputAttributeDescription(1, 1, VkFormat.VK_FORMAT_R32G32B32A32_SFLOAT, 0),
            VkVertexInputAttributeDescription(2, 2, VkFormat.VK_FORMAT_R32G32_SFLOAT, 0))
}

fun create2DVertexInputState(): VkPipelineVertexInputStateCreateInfo {
    return VkPipelineVertexInputStateCreateInfo(0, create2DDescriptions(), create2DAttributes())
}
