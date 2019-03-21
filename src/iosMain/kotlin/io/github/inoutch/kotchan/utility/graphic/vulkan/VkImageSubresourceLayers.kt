package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun VkImageSubresourceLayers.copyToNative(native: vulkan.VkImageSubresourceLayers) {
    native.aspectMask = aspectMask.sumBy { it.value }.toUInt()
    native.mipLevel = mipLevel.toUInt()
    native.baseArrayLayer = baseArrayLayer.toUInt()
    native.layerCount = layerCount.toUInt()
}
