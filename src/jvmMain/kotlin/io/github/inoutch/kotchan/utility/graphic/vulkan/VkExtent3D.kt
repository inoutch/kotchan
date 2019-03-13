package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkExtent3D.copyToNative(native: org.lwjgl.vulkan.VkExtent3D) {
    native.width(width)
            .height(height)
            .depth(depth)
}

fun VkExtent3D.toNative(scope: MemScope): org.lwjgl.vulkan.VkExtent3D =
        scope.add(org.lwjgl.vulkan.VkExtent3D.calloc()
                .also { copyToNative(it) })
