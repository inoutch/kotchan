package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_MEMORY_BARRIER

@ExperimentalUnsignedTypes
fun VkMemoryBarrier.copyToNative(native: vulkan.VkMemoryBarrier) {
    native.sType = VK_STRUCTURE_TYPE_MEMORY_BARRIER
    native.pNext = null
    native.srcAccessMask = srcAccessFlags.sumBy { it.value }.toUInt()
    native.dstAccessMask = dstAccessFlags.sumBy { it.value }.toUInt()
}

@ExperimentalUnsignedTypes
fun VkMemoryBarrier.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkMemoryBarrier>().also { copyToNative(it) }

@ExperimentalUnsignedTypes
fun List<VkMemoryBarrier>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkMemoryBarrier>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
