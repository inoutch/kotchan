package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkImageSubresourceRange.copyToNative(native: vulkan.VkImageSubresourceRange) {
    native.aspectMask = aspectMask.sumBy { it.value }.toUInt()
    native.baseMipLevel = baseMipLevel.toUInt()
    native.levelCount = levelCount.toUInt()
    native.baseArrayLayer = baseArrayLayer.toUInt()
    native.layerCount = layerCount.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkImageSubresourceRange>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkImageSubresourceRange>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
