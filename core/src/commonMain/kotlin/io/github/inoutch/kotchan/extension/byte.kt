package io.github.inoutch.kotchan.extension

expect fun ByteArray.toUTF8String(): String

fun IntArray.toByteArray(): ByteArray {
    return foldIndexed(ByteArray(size)) { i, a, v -> a.apply { set(i, v.toByte()) } }
}
