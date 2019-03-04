package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class MappedMemory(size: Long) : Disposable {
    val bytesSize: Long

    fun copy(offset: Long, size: Long, array: FloatArray)

    fun copy(offset: Long, size: Long, array: IntArray)
}
