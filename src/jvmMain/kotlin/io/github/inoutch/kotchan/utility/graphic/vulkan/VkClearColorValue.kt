package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import io.github.inoutch.kotchan.utility.type.Vector4
import org.lwjgl.vulkan.VkClearColorValue

fun Vector4.copyToNative(native: VkClearColorValue) {
    native.float32(0, x)
    native.float32(1, y)
    native.float32(2, z)
    native.float32(3, w)
}

fun Vector4.toNative(scope: MemScope): VkClearColorValue = scope.add(VkClearColorValue.calloc().also { copyToNative(it) })
