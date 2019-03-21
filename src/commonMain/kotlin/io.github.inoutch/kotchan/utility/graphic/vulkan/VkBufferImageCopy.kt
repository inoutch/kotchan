package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkBufferImageCopy(
        val bufferOffset: Long,
        val bufferRowLength: Int,
        val bufferImageHeight: Int,
        val imageSubresource: VkImageSubresourceLayers,
        val imageOffset: VkOffset3D,
        val imageExtent: VkExtent3D)
