package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.utility.type.Point

actual class Image actual constructor(byteArray: ByteArray, size: Point) {
    actual companion object {
        actual fun load(byteArray: ByteArray): Image {
            TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
        }
    }

    actual val byteArray: ByteArray
        get() = TODO("not implemented") // To change initializer of created properties use File | Settings | File Templates.
    actual val size: Point
        get() = TODO("not implemented") // To change initializer of created properties use File | Settings | File Templates.
}
