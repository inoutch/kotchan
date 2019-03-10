package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.type.Vector4
import io.github.inoutch.kotchan.utility.type.Version
import kotlinx.cinterop.*
import kotlinx.cinterop.UIntVar

@ExperimentalUnsignedTypes
fun Version.toNative(): UInt {
    return (major.toUInt() shl 22) or (minor.toUInt() shl 12) or (patch.toUInt())
}

@ExperimentalUnsignedTypes
fun Boolean.toVkBool32() = if (this) 1u else 0u

@ExperimentalUnsignedTypes
fun UInt.toBoolean() = this != 0u

fun Vector4.copyToNative(native: vulkan.VkClearColorValue) {
    native.float32[0] = x
    native.float32[1] = y
    native.float32[2] = z
    native.float32[3] = w
}

fun Vector4.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkClearColorValue>()
                .also { copyToNative(it) }.ptr
