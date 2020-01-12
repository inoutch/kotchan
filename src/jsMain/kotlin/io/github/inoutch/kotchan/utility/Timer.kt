package io.github.inoutch.kotchan.utility

import kotlin.js.Date

actual class Timer {
    actual companion object {
        actual fun milliseconds(): Long {
            return Date.now().toLong()
        }
    }
}
