package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkComponentMapping.copyToNative(native: org.lwjgl.vulkan.VkComponentMapping) {
    native.r(r.value)
            .g(g.value)
            .b(b.value)
            .a(a.value)
}

fun VkComponentMapping.toNative(scope: MemScope): org.lwjgl.vulkan.VkComponentMapping =
        scope.add(org.lwjgl.vulkan.VkComponentMapping.calloc()
                .also { copyToNative(it) })
