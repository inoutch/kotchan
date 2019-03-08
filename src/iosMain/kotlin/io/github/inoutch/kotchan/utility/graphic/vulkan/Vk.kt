package io.github.inoutch.kotchan.utility.graphic.vulkan

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
