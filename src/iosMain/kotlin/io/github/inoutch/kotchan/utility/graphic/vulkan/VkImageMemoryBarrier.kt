package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkImageMemoryBarrier.copyToNative(native: vulkan.VkImageMemoryBarrier) {
    native.srcAccessMask = srcAccessMask.sumBy { it.value }.toUInt()
    native.dstAccessMask = dstAccessMask.sumBy { it.value }.toUInt()
    native.oldLayout = oldLayout.value.toUInt()
    native.newLayout = newLayout.value.toUInt()
    native.srcQueueFamilyIndex = srcQueueFamilyIndex.toUInt()
    native.dstQueueFamilyIndex = dstQueueFamilyIndex.toUInt()
    native.image = image.native
    subresourceRange.copyToNative(native.subresourceRange)
}

@ExperimentalUnsignedTypes
fun List<VkImageMemoryBarrier>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkImageMemoryBarrier>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
