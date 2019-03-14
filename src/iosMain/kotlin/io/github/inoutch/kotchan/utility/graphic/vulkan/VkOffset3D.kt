package io.github.inoutch.kotchan.utility.graphic.vulkan

fun VkOffset3D.copyToNative(native: vulkan.VkOffset3D) {
    native.x = x
    native.y = y
    native.z = z
}
