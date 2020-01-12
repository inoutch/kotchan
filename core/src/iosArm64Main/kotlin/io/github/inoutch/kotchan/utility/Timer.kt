package io.github.inoutch.kotchan.utility

import platform.Foundation.NSDate
import platform.Foundation.date
import platform.Foundation.timeIntervalSince1970

actual class Timer {
    actual companion object {
        actual fun milliseconds(): Long {
            val now = NSDate.date().timeIntervalSince1970() * 1000.0f
            return now.toLong()
        }
    }
}
