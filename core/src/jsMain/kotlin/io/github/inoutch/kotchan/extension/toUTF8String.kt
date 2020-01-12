package io.github.inoutch.kotchan.extension

@ExperimentalStdlibApi
actual fun ByteArray.toUTF8String(): String {
    return decodeToString()
}
