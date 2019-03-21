package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkOffset3D.copyToNative(native: org.lwjgl.vulkan.VkOffset3D) {
    native.set(x, y, z)
}

fun VkOffset3D.toNative(scope: MemScope): org.lwjgl.vulkan.VkOffset3D =
        scope.add(org.lwjgl.vulkan.VkOffset3D.calloc()
                .also { copyToNative(it) })
