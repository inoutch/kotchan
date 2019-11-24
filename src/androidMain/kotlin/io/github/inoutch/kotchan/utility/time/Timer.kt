package io.github.inoutch.kotchan.utility.time

actual class Timer {
    actual companion object {
        actual fun milliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
}
