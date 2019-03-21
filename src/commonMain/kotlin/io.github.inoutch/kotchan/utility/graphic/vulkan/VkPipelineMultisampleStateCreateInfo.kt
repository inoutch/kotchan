package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineMultisampleStateCreateInfo(
        val flags: Int,
        val rasterizationSamples: List<VkSampleCountFlagBits>,
        val sampleShadingEnable: Boolean,
        val minSampleShading: Float,
        val sampleMask: Int?,
        val alphaToCoverageEnable: Boolean,
        val alphaToOneEnable: Boolean)
