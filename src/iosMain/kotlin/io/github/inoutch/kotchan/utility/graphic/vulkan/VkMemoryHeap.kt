package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun vulkan.VkMemoryHeap.toOrigin(): VkMemoryHeap {
    return VkMemoryHeap(size.toLong(), flags.toInt())
}
