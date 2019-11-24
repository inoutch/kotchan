package io.github.inoutch.kotchan.utility.time

import platform.Foundation.*

actual class Timer {
    actual companion object {
        actual fun milliseconds(): Long {
            val now = NSDate.date().timeIntervalSince1970() * 1000.0f
            return now.toLong()
        }
    }
}
