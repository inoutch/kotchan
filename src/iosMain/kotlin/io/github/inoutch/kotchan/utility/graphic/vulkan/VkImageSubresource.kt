package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkImageSubresource.copyToNative(native: vulkan.VkImageSubresource) {
    native.aspectMask = aspectMask.sumBy { it.value }.toUInt()
    native.arrayLayer = arrayLayer.toUInt()
    native.mipLevel = mipLevel.toUInt()
}

@ExperimentalUnsignedTypes
fun VkImageSubresource.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkImageSubresource>()
                .also { copyToNative(it) }.ptr
