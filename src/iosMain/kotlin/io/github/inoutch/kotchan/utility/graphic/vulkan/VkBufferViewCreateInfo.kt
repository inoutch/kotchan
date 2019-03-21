package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_BUFFER_VIEW_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkBufferViewCreateInfo.copyToNative(native: vulkan.VkBufferViewCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_BUFFER_VIEW_CREATE_INFO
    native.pNext = null
    native.flags = flags.sumBy { it.value }.toUInt()
    native.buffer = buffer.native
    native.format = format.value.toUInt()
    native.offset = offset.toULong()
    native.range = range.toULong()
}

@ExperimentalUnsignedTypes
fun VkBufferViewCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkBufferViewCreateInfo>()
                .also { copyToNative(it) }.ptr
