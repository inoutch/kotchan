package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineViewportStateCreateInfo(
    val flags: Int,
    val viewportCount: Int,
    val viewports: List<VkViewport>,
    val scissorCount: Int,
    val scissors: List<VkRect2D>
)
