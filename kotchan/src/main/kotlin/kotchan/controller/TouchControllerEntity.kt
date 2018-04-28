package kotchan.controller

import kotchan.Engine
import kotchan.controller.touchable.Touchable
import utility.type.Vector2

class TouchControllerEntity : TouchEmitter, TouchController {
    companion object {
        fun convertNormalPointInView(p: Vector2): Vector2 {
            return p / Engine.getInstance().windowSize * 2.0f - 1.0f
        }
    }

    // about touchable
    private val touchables: MutableList<Touchable> = mutableListOf()

    // about touch
    private val touches: MutableMap<TouchEvent, TouchEntity> = mutableMapOf()
    private var incremental = 0

    override fun onTouchesBegan(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = TouchEntity(incremental++, it.point, TouchType.Began)
            touches[it] = touch
            onTouch(touchables, touch)
        }
    }

    override fun onTouchesMoved(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return@forEach
            touch.point = it.point
            touch.type = TouchType.Moved
            onTouch(touchables, touch)
        }
    }

    override fun onTouchesEnded(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return
            touch.point = it.point
            touch.type = TouchType.Ended
            onTouch(touchables, touch)
        }
        touchEvents.forEach { touches.remove(it) }
        if (touches.isEmpty()) {
            incremental = 0
        }
    }

    override fun onTouchesCancelled() {
        incremental = 0
        touchables.forEach { it.on(TouchEntity(-1, Vector2(), TouchType.Cancelled), false) }
    }

    override fun add(touchable: Touchable, priority: Int) {
        touchable.priority = priority
        touchables.add(touchable)
    }

    override fun clearAll() {
        touchables.clear()
    }

    fun update(delta: Float) {
        touchables.forEach { it.update(delta) }
    }

    private fun onTouch(touchables: List<Touchable>, toucheEntity: TouchEntity) {
        val sortedTouchable = touchables
                .filter { it.enable }
                .sortedBy { -it.priority }
        var currentChain = true
        var nextChain = true
        var priority = Int.MAX_VALUE
        for (touchable in sortedTouchable) {
            if (priority > touchable.priority) {
                currentChain = nextChain
                priority = touchable.priority
            }
            nextChain = nextChain.and(touchable.on(toucheEntity, currentChain))
        }
    }
}