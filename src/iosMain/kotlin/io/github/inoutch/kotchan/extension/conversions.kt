package io.github.inoutch.kotchan.extension

actual fun ByteArray.toUTF8String(): String {
    return this.stringFromUtf8()
}
