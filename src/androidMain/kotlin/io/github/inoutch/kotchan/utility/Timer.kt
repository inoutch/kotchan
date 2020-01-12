package io.github.inoutch.kotchan.utility

actual class Timer {
    actual companion object {
        actual fun milliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
}
