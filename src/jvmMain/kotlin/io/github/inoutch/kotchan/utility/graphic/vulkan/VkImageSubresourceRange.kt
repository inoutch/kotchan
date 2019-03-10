package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkImageSubresourceRange.copyToNative(native: org.lwjgl.vulkan.VkImageSubresourceRange) {
    native.aspectMask(aspectMask.sumBy { it.value })
            .baseMipLevel(baseMipLevel)
            .levelCount(levelCount)
            .baseArrayLayer(baseArrayLayer)
            .layerCount(layerCount)
}

fun VkImageSubresourceRange.toNative(scope: MemScope): org.lwjgl.vulkan.VkImageSubresourceRange =
        scope.add(org.lwjgl.vulkan.VkImageSubresourceRange.calloc()
                .also { copyToNative(it) })

fun List<VkImageSubresourceRange>.toNative(scope: MemScope): org.lwjgl.vulkan.VkImageSubresourceRange.Buffer =
        scope.add(org.lwjgl.vulkan.VkImageSubresourceRange.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
