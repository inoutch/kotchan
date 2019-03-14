package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import kotlinx.cinterop.allocArray

@ExperimentalUnsignedTypes
fun VkDescriptorBufferInfo.copyToNative(native: vulkan.VkDescriptorBufferInfo) {
    native.buffer = buffer.native
    native.offset = offset.toULong()
    native.range = range.toULong()
}

@ExperimentalUnsignedTypes
fun List<VkDescriptorBufferInfo>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkDescriptorBufferInfo>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
