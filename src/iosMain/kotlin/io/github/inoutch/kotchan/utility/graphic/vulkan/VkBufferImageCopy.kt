package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkBufferImageCopy.copyToNative(native: vulkan.VkBufferImageCopy) {
    native.bufferOffset = bufferOffset.toULong()
    native.bufferImageHeight = bufferImageHeight.toUInt()
    native.bufferRowLength = bufferRowLength.toUInt()
    imageExtent.copyToNative(native.imageExtent)
    imageOffset.copyToNative(native.imageOffset)
    imageSubresource.copyToNative(native.imageSubresource)
}

@ExperimentalUnsignedTypes
fun VkBufferImageCopy.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkBufferImageCopy>()
                .also { copyToNative(it) }.ptr

@ExperimentalUnsignedTypes
fun List<VkBufferImageCopy>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkBufferImageCopy>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
