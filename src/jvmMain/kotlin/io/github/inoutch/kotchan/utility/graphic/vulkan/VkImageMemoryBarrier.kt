package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkImageMemoryBarrier.copyToNative(
        native: org.lwjgl.vulkan.VkImageMemoryBarrier,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER)
            .pNext(VK10.VK_NULL_HANDLE)
            .srcAccessMask(srcAccessMask.sumBy { it.value })
            .dstAccessMask(dstAccessMask.sumBy { it.value })
            .oldLayout(oldLayout.value)
            .newLayout(newLayout.value)
            .srcQueueFamilyIndex(srcQueueFamilyIndex)
            .dstQueueFamilyIndex(dstQueueFamilyIndex)
            .image(image.native)
            .subresourceRange(subresourceRange.toNative(scope))
}

fun List<VkImageMemoryBarrier>.toNative(scope: MemScope): org.lwjgl.vulkan.VkImageMemoryBarrier.Buffer? =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkImageMemoryBarrier.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })
