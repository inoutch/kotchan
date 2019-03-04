package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkAttachmentReference(
        val attachment: Int, // index
        val layout: VkImageLayout)
