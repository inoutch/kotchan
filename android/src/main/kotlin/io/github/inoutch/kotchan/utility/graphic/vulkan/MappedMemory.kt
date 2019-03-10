package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable


actual class MappedMemory actual constructor(size: Long) : Disposable {
    actual val bytesSize: Long
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    actual fun copy(offset: Long, size: Long, array: FloatArray) {
    }

    actual fun copy(offset: Long, size: Long, array: IntArray) {
    }

    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
