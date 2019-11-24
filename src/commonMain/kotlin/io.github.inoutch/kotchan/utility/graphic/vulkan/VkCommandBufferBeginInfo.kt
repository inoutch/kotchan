package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkCommandBufferBeginInfo(
    val flags: List<VkCommandBufferUsageFlagBits>,
    val inheritanceInfo: VkCommandBufferInheritanceInfo?
)
