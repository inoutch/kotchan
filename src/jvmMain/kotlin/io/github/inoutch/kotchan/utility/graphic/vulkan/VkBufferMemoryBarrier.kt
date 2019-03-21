package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkBufferMemoryBarrier.copyToNative(native: org.lwjgl.vulkan.VkBufferMemoryBarrier) {
    native.sType(VK10.VK_STRUCTURE_TYPE_BUFFER_MEMORY_BARRIER)
            .pNext(VK10.VK_NULL_HANDLE)
            .srcAccessMask(srcAccessMask.sumBy { it.value })
            .dstAccessMask(dstAccessMask.sumBy { it.value })
            .srcQueueFamilyIndex(srcQueueFamilyIndex)
            .dstQueueFamilyIndex(dstQueueFamilyIndex)
            .buffer(buffer.native)
            .offset(offset)
            .size(size)
}

fun List<VkBufferMemoryBarrier>.toNative(scope: MemScope): org.lwjgl.vulkan.VkBufferMemoryBarrier.Buffer? =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkBufferMemoryBarrier.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
