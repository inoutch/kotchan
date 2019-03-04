package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkMemoryRequirements(
        val size: Long,
        val alignment: Long,
        val memoryTypeBits: Int)

expect fun vkGetBufferMemoryRequirements(device: VkDevice, buffer: VkBuffer): VkMemoryRequirements
