package io.github.inoutch.kotchan.extension

import kotlinx.cinterop.toKString

actual fun ByteArray.toUTF8String(): String {
    return this.toKString()
}
