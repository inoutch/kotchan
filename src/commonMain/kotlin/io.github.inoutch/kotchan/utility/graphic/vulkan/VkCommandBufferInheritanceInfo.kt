package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkCommandBufferInheritanceInfo(
        val renderPass: VkRenderPass,
        val subpass: Int, // subpass index
        val framebuffer: VkFramebuffer,
        val occlusionQueryEnable: Boolean,
        val queryFlags: List<VkQueryControlFlagBits>,
        val pipelineStatistics: List<VkQueryPipelineStatisticFlagBits>)
