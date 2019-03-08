package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun VkImageSubresourceRange.copyToNative(native: vulkan.VkImageSubresourceRange) {
    native.aspectMask = aspectMask.sumBy { it.value }.toUInt()
    native.baseMipLevel = baseMipLevel.toUInt()
    native.levelCount = levelCount.toUInt()
    native.baseArrayLayer = baseArrayLayer.toUInt()
    native.layerCount = layerCount.toUInt()
}
