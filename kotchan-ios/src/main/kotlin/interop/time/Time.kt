package interop.time

import platform.Foundation.*

actual class Time {
    actual companion object {
        actual fun milliseconds(): Long {
            val now = NSDate.date().timeIntervalSince1970() * 1000.0f
            return now.toLong()
        }
    }
}