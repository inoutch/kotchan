package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineRasterizationStateCreateInfo(
    val flags: Int,
    val polygonMode: VkPolygonMode,
    val cullMode: VkCullMode,
    val frontFace: VkFrontFace,
    val depthClampEnable: Boolean,
    val rasterizerDiscardEnable: Boolean,
    val depthBiasEnable: Boolean,
    val depthBiasConstantFactor: Float,
    val depthBiasClamp: Float,
    val depthBiasSlopeFactor: Float,
    val lineWidth: Float
)
