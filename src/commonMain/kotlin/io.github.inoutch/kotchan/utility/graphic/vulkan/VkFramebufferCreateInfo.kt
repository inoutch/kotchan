package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkFramebufferCreateInfo(
        val flags: Int,
        val renderPass: VkRenderPass,
        val attachments: List<VkImageView>,
        val width: Int,
        val height: Int,
        val layers: Int)
