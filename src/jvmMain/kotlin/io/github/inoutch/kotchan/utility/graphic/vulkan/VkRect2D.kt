package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import io.github.inoutch.kotchan.utility.type.Point

fun Point.copyToNativeOffset(native: org.lwjgl.vulkan.VkOffset2D) {
    native.set(x, y)
}

fun Point.toNativeOffset(scope: MemScope): org.lwjgl.vulkan.VkOffset2D =
        scope.add(org.lwjgl.vulkan.VkOffset2D.calloc().also { copyToNativeOffset(it) })

fun Point.copyToNativeExtent(native: org.lwjgl.vulkan.VkExtent2D) {
    native.set(x, y)
}

fun Point.toNativeExtent(scope: MemScope): org.lwjgl.vulkan.VkExtent2D =
        scope.add(org.lwjgl.vulkan.VkExtent2D.calloc().also { copyToNativeExtent(it) })

fun VkRect2D.copyToNative(native: org.lwjgl.vulkan.VkRect2D, scope: MemScope) {
    native.offset(offset.toNativeOffset(scope))
            .extent(extent.toNativeExtent(scope))
}

fun VkRect2D.toNative(scope: MemScope): org.lwjgl.vulkan.VkRect2D =
        scope.add(org.lwjgl.vulkan.VkRect2D.calloc()
                .also { copyToNative(it, scope) })

fun List<VkRect2D>.toNative(scope: MemScope): org.lwjgl.vulkan.VkRect2D.Buffer? =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkRect2D.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })

fun org.lwjgl.vulkan.VkOffset2D.toOrigin() =
        Point(x(), y())

fun org.lwjgl.vulkan.VkExtent2D.toOrigin() =
        Point(width(), height())
