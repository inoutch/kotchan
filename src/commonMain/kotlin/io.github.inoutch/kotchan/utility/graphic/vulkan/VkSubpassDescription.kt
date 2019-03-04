package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSubpassDescription(
        val flags: Int,
        val pipelineBindPoint: VkPipelineBindPoint,
        val inputAttachments: List<VkAttachmentReference>,
        val colorAttachments: List<VkAttachmentReference>,
        val resolveAttachments: List<VkAttachmentReference>,
        val depthStencilAttachment: VkAttachmentReference?,
        val preserveAttachments: List<Int>)
