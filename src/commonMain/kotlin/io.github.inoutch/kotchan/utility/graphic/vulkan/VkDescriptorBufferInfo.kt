package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkDescriptorBufferInfo(
    val buffer: VkBuffer,
    val offset: Long,
    val range: Long
)
