package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSubpassDependency(
        val srcSubpassIndex: Int,
        val dstSubpassIndex: Int,
        val srcStageMask: VkPipelineStageFlagBits,
        val dstStageMask: VkPipelineStageFlagBits,
        val srcAccessMask: VkPipelineStageFlagBits,
        val dstAccessMask: VkPipelineStageFlagBits,
        val dependencyFlags: List<VkDependencyFlagBits>)
