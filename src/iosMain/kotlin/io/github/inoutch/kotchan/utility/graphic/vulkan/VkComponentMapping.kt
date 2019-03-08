package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun VkComponentMapping.copyToNative(native: vulkan.VkComponentMapping) {
    native.r = r.value.toUInt()
    native.g = g.value.toUInt()
    native.b = b.value.toUInt()
    native.a = a.value.toUInt()
}
