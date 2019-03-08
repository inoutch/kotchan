package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkRect2D.copyToNative(native: vulkan.VkRect2D) {
    native.offset.x = offset.x
    native.offset.y = offset.y
    native.extent.width = extent.x.toUInt()
    native.extent.height = extent.y.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkRect2D>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkRect2D>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
