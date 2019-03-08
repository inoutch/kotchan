package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.type.Point

@ExperimentalUnsignedTypes
fun vulkan.VkExtent2D.toOrigin(): Point {
    return Point(width.toInt(), height.toInt())
}

@ExperimentalUnsignedTypes
fun Point.copyToNative(native: vulkan.VkExtent2D) {
    native.width = x.toUInt()
    native.height = y.toUInt()
}
