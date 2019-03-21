package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun VkExtent3D.copyToNative(native: vulkan.VkExtent3D) {
    native.width = width.toUInt()
    native.height = height.toUInt()
    native.depth = depth.toUInt()
}

@ExperimentalUnsignedTypes
fun vulkan.VkExtent3D.toOrigin(): VkExtent3D {
    return VkExtent3D(width.toInt(), height.toInt(), depth.toInt())
}
