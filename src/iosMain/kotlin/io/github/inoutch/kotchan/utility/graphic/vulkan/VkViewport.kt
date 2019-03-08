package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

fun VkViewport.copyToNative(native: vulkan.VkViewport) {
    native.x = x
    native.y = y
    native.width = width
    native.height = height
    native.maxDepth = maxDepth
    native.minDepth = minDepth
}

fun List<VkViewport>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkViewport>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
