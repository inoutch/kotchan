package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPushConstantRange(
    val stageFlags: List<VkShaderStageFlagBits>,
    val offset: Int,
    val size: Int
)
