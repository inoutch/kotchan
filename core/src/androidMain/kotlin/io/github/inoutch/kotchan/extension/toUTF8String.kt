package io.github.inoutch.kotchan.extension

import java.nio.charset.Charset

actual fun ByteArray.toUTF8String(): String {
    return toString(Charset.forName("UTF8"))
}
