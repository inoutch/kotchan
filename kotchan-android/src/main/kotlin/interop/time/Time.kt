package interop.time

actual class Time {
    actual companion object {
        actual fun milliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
}