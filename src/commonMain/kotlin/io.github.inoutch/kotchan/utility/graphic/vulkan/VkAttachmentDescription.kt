package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkAttachmentDescription(
        val flags: Int,
        val format: VkFormat,
        val samples: List<VkSampleFlagBits>,
        val loadOp: VkAttachmentLoadOp,
        val storeOp: VkAttachmentStoreOp,
        val stencilLoadOp: VkAttachmentLoadOp,
        val stencilStoreOp: VkAttachmentStoreOp,
        val initialLayout: VkImageLayout,
        val finalLayout: VkImageLayout)
