package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPipelineDynamicStateCreateInfo(
    val flags: Int,
    val dynamicStates: List<VkDynamicState>
)
