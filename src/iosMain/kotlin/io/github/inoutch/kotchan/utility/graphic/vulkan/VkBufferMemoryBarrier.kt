package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_BUFFER_MEMORY_BARRIER

@ExperimentalUnsignedTypes
fun VkBufferMemoryBarrier.copyToNative(native: vulkan.VkBufferMemoryBarrier) {
    native.sType = VK_STRUCTURE_TYPE_BUFFER_MEMORY_BARRIER
    native.pNext = null
    native.srcAccessMask = srcAccessMask.sumBy { it.value }.toUInt()
    native.dstAccessMask = dstAccessMask.sumBy { it.value }.toUInt()
    native.srcQueueFamilyIndex = srcQueueFamilyIndex.toUInt()
    native.dstQueueFamilyIndex = dstQueueFamilyIndex.toUInt()
    native.buffer = buffer.native
    native.offset = offset.toULong()
    native.size = size.toULong()
}

@ExperimentalUnsignedTypes
fun List<VkBufferMemoryBarrier>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkBufferMemoryBarrier>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
