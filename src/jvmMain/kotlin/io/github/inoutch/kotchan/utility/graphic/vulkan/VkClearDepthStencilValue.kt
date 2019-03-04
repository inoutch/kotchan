package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkClearDepthStencilValue.copyToNative(native: org.lwjgl.vulkan.VkClearDepthStencilValue) {
    native.depth(depth)
            .stencil(stencil)
}

fun VkClearDepthStencilValue.toNative(scope: MemScope): org.lwjgl.vulkan.VkClearDepthStencilValue =
        scope.add(org.lwjgl.vulkan.VkClearDepthStencilValue.calloc()
                .also { copyToNative(it) })
