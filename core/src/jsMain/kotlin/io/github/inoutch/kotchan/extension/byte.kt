package io.github.inoutch.kotchan.extension

import org.khronos.webgl.Uint8ClampedArray
import org.khronos.webgl.get

fun Uint8ClampedArray.toByteArray(): ByteArray {
    val byteArray = ByteArray(length)
    for (i in 0 until length) {
        byteArray[i] = get(i)
    }
    return byteArray
}
