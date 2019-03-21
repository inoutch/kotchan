package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkImageSubresource.copyToNative(native: org.lwjgl.vulkan.VkImageSubresource) {
    native.set(aspectMask.sumBy { it.value }, mipLevel, arrayLayer)
}

fun VkImageSubresource.toNative(scope: MemScope): org.lwjgl.vulkan.VkImageSubresource =
        scope.add(org.lwjgl.vulkan.VkImageSubresource.calloc()
                .also { copyToNative(it) })
