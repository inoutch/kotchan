package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineDepthStencilStateCreateInfo(
        val flags: Int,
        val depthTestEnable: Boolean,
        val depthWriteEnable: Boolean,
        val depthCompareOp: VkCompareOp,
        val depthBoundsTestEnable: Boolean,
        val stencilTestEnable: Boolean,
        val front: VkStencilOpState,
        val back: VkStencilOpState,
        val minDepthBounds: Float,
        val maxDepthBounds: Float)
