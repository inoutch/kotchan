package io.github.inoutch.kotchan.extension

import java.nio.charset.Charset

actual fun ByteArray.toUTF8String(): String {
    return String(this, Charset.forName("utf-8"))
}