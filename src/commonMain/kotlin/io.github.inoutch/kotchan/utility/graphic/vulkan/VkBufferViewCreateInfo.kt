package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkBufferViewCreateInfo(
        val flags: List<VkBufferViewCreateFlagBits>,
        val buffer: VkBuffer,
        val format: VkFormat,
        val offset: Long,
        val range: Long)
