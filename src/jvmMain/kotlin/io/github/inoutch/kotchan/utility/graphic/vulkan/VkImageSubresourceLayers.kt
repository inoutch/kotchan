package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkImageSubresourceLayers.copyToNative(native: org.lwjgl.vulkan.VkImageSubresourceLayers) {
    native.aspectMask(aspectMask.sumBy { it.value })
            .mipLevel(mipLevel)
            .baseArrayLayer(baseArrayLayer)
            .layerCount(layerCount)
}

fun VkImageSubresourceLayers.toNative(scope: MemScope): org.lwjgl.vulkan.VkImageSubresourceLayers =
        scope.add(org.lwjgl.vulkan.VkImageSubresourceLayers.calloc()
                .also { copyToNative(it) })
