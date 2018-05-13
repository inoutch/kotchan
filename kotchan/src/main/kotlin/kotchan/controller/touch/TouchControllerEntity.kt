package kotchan.controller.touch

import kotchan.Engine
import kotchan.controller.touch.listener.TouchListener
import utility.type.Vector2

class TouchControllerEntity : TouchEmitter, TouchController {
    companion object {
        fun convertNormalPointInView(p: Vector2): Vector2 {
            return p / Engine.getInstance().windowSize * 2.0f - 1.0f
        }
    }

    // about listener
    private val touchListeners: MutableList<TouchListener> = mutableListOf()

    // about touch
    private val touches: MutableMap<TouchEvent, TouchEntity> = mutableMapOf()
    private var incremental = 0

    override fun onTouchesBegan(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = TouchEntity(incremental++, it.point, TouchType.Began)
            touches[it] = touch
            onTouch(touchListeners, touch)
        }
    }

    override fun onTouchesMoved(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return@forEach
            touch.point = it.point
            touch.type = TouchType.Moved
            onTouch(touchListeners, touch)
        }
    }

    override fun onTouchesEnded(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return
            touch.point = it.point
            touch.type = TouchType.Ended
            onTouch(touchListeners, touch)
        }
        touchEvents.forEach { touches.remove(it) }
        if (touches.isEmpty()) {
            incremental = 0
        }
    }

    override fun onTouchesCancelled() {
        incremental = 0
        touches.clear()
        touchListeners.forEach { it.on(TouchEntity(-1, Vector2(), TouchType.Cancelled), false) }
    }

    override fun add(touchListener: TouchListener, priority: Int) {
        touchListener.priority = priority
        touchListeners.add(touchListener)
    }

    override fun clearAll() {
        touchListeners.clear()
    }

    fun update(delta: Float) {
        touchListeners.forEach { it.update(delta) }
    }

    private fun onTouch(touchListeners: List<TouchListener>, toucheEntity: TouchEntity) {
        val sortedTouchListener = touchListeners
                .filter { it.enable }
                .sortedBy { -it.priority }
        var currentChain = true
        var nextChain = true
        var priority = Int.MAX_VALUE
        for (touchListener in sortedTouchListener) {
            if (priority > touchListener.priority) {
                currentChain = nextChain
                priority = touchListener.priority
            }
            nextChain = nextChain.and(touchListener.on(toucheEntity, currentChain))
        }
    }
}