package io.github.inoutch.kotchan.extension

fun IntArray.toByteArray(): ByteArray {
    return foldIndexed(ByteArray(size)) { i, a, v -> a.apply { set(i, v.toByte()) } }
}
