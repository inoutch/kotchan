package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSubpassDependency(
    val srcSubpassIndex: Int,
    val dstSubpassIndex: Int,
    val srcStageMask: List<VkPipelineStageFlagBits>,
    val dstStageMask: List<VkPipelineStageFlagBits>,
    val srcAccessMask: List<VkAccessFlagBits>,
    val dstAccessMask: List<VkAccessFlagBits>,
    val dependencyFlags: List<VkDependencyFlagBits>
)
