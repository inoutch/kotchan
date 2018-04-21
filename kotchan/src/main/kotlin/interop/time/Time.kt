package interop.time

expect class Time {
    companion object {
        fun milliseconds(): Long
    }
}