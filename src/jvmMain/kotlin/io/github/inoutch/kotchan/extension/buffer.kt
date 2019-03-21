package io.github.inoutch.kotchan.extension

import java.nio.ByteBuffer

fun ByteBuffer.toByteArray(): ByteArray {
    val byteArray = ByteArray(capacity())
    get(byteArray, 0, byteArray.size)
    return byteArray
}
