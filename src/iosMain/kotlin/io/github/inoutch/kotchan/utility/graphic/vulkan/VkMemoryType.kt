package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun vulkan.VkMemoryType.toOrigin(): VkMemoryType {
    return VkMemoryType(heapIndex.toInt(), propertyFlags.toInt())
}
