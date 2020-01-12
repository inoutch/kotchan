package io.github.inoutch.kotchan.utility

expect class Timer {
    companion object {
        fun milliseconds(): Long
    }
}

class TimerInterval {
    private val start = Timer.milliseconds()

    fun lap(): Long {
        return Timer.milliseconds() - start
    }
}

inline fun Timer.Companion.measure(scope: (interval: TimerInterval) -> Unit) {
    scope(TimerInterval())
}
