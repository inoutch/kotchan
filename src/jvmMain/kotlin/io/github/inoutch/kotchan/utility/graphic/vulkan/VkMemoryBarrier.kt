package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkMemoryBarrier.copyToNative(native: org.lwjgl.vulkan.VkMemoryBarrier) {
    native.sType(VK10.VK_STRUCTURE_TYPE_MEMORY_BARRIER)
            .pNext(VK10.VK_NULL_HANDLE)
            .srcAccessMask(srcAccessFlags.sumBy { it.value })
            .dstAccessMask(dstAccessFlags.sumBy { it.value })
}

fun List<VkMemoryBarrier>.toNative(scope: MemScope): org.lwjgl.vulkan.VkMemoryBarrier.Buffer? =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkMemoryBarrier.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
