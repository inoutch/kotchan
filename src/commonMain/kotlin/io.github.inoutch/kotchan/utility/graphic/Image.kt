package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.utility.io.readBytesFromResourceWithError
import io.github.inoutch.kotchan.utility.type.Point

expect class Image private constructor(byteArray: ByteArray, size: Point) {
    companion object {
        fun load(byteArray: ByteArray): Image
    }

    val byteArray: ByteArray

    val size: Point
}

fun Image.Companion.loadFromResource(filepath: String) = load(instance.file.readBytesFromResourceWithError(filepath))
