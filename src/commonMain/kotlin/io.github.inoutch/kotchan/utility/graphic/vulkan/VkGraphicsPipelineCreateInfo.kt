package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkGraphicsPipelineCreateInfo(
        val flags: Int,
        val stages: List<VkPipelineShaderStageCreateInfo>,
        val vertexInputState: VkPipelineVertexInputStateCreateInfo,
        val inputAssemblyState: VkPipelineInputAssemblyStateCreateInfo,
        val tessellationState: VkPipelineTessellationStateCreateInfo?,
        val viewportState: VkPipelineViewportStateCreateInfo,
        val rasterizationState: VkPipelineRasterizationStateCreateInfo,
        val multisampleState: VkPipelineMultisampleStateCreateInfo,
        val depthStencilState: VkPipelineDepthStencilStateCreateInfo,
        val colorBlendState: VkPipelineColorBlendStateCreateInfo,
        val dynamicState: VkPipelineDynamicStateCreateInfo,
        val layout: VkPipelineLayout,
        val renderPass: VkRenderPass,
        val subpass: Int, // the index of the subpass in the render pass where this pipeline will be used
        val basePipelineHandle: VkPipeline?,
        val basePipelineIndex: Int?)
