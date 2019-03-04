package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineLayoutCreateInfo(
        val flags: Int,
        val setLayouts: List<VkDescriptorSetLayout>,
        val pushConstantRanges: List<VkPushConstantRange>)
