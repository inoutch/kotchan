package io.github.inoutch.kotchan.core.controller.touch

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.core.controller.touch.listener.TouchListener

class TouchControllerEntity : TouchEmitter, TouchController {
    companion object {
        fun convertNormalPointInView(p: Vector2): Vector2 {
            return p / KotchanCore.instance.windowSize * 2.0f - 1.0f
        }
    }

    // about listener
    private val touchListeners: MutableList<TouchListener> = mutableListOf()

    // about touch
    private val touches: MutableMap<TouchEvent, TouchEntity> = mutableMapOf()

    private var incremental = 0

    override val touchSize: Int
        get() = touches.size

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
        touchListeners.forEach { it.on(TouchEntity(-1, Point(), TouchType.Cancelled), false) }
    }

    override fun add(touchListener: TouchListener, priority: Int) {
        touchListener.priority = priority
        touchListeners.add(touchListener)
    }

    override fun remove(touchListener: TouchListener) {
        touchListeners.remove(touchListener)
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
        var nextChain = true
        for (touchListener in sortedTouchListener) {
            nextChain = nextChain.and(touchListener.on(toucheEntity, nextChain))
        }
    }
}
