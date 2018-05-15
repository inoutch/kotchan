package kotchan.controller.event.listener

class TimerEventController {
    private var currentTime = 0L
    private val magnificationForAccuracy = 3.0f * 60.0f
    private val events: MutableMap<Long, MutableList<() -> Unit>> = mutableMapOf()

    fun update(delta: Float) {// 0.016
        val after = currentTime + (delta * magnificationForAccuracy).toLong()
        for (n in currentTime + 1..after) {
            events[n]?.forEach { it() }
            events[n]?.clear()
        }
        currentTime = after
    }

    fun subscribeOnce(seconds: Float, callback: () -> Unit) {
        val time = currentTime + (seconds * magnificationForAccuracy).toLong()
        events.getOrPut(time, { mutableListOf() }).add(callback)
    }
}