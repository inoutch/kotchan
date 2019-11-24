package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineColorBlendAttachmentState(
    val blendEnable: Boolean,
    val srcColorBlendFactor: VkBlendFactor,
    val dstColorBlendFactor: VkBlendFactor,
    val colorBlendOp: VkBlendOp,
    val srcAlphaBlendFactor: VkBlendFactor,
    val dstAlphaBlendFactor: VkBlendFactor,
    val alphaBlendOp: VkBlendOp,
    val colorWriteMask: List<VkColorComponentFlagBits>
)
