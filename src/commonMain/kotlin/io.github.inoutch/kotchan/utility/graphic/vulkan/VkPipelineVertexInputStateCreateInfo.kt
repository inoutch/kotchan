package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineVertexInputStateCreateInfo(
        val flags: Int,
        val vertexBindingDescriptions: List<VkVertexInputBindingDescription>,
        val vertexAttributeDescriptions: List<VkVertexInputAttributeDescription>)
