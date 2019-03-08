package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkClearValue.copyToNative(native: vulkan.VkClearValue) {
    native.color.float32[0] = color.x
    native.color.float32[1] = color.y
    native.color.float32[2] = color.z
    native.color.float32[3] = color.w
    native.depthStencil.depth = depthStencil.depth
    native.depthStencil.stencil = depthStencil.stencil.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkClearValue>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkClearValue>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
