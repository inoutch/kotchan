package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkClearDepthStencilValue.copyToNative(native: vulkan.VkClearDepthStencilValue) {
    native.depth = depth
    native.stencil = stencil.toUInt()
}

@ExperimentalUnsignedTypes
fun VkClearDepthStencilValue.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkClearDepthStencilValue>()
                .also { copyToNative(it) }.ptr
