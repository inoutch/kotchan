package kotchan.controller

import kotchan.Engine
import kotchan.controller.touchable.Touchable
import kotchan.logger.logger
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
            touchables.filter { it.enable }
                    .forEach { it.on(touch) }
        }
    }

    override fun onTouchesMoved(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return@forEach
            touch.point = it.point
            touch.type = TouchType.Moved
            touchables.filter { it.enable }
                    .forEach { it.on(touch) }
        }
    }

    override fun onTouchesEnded(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return
            touch.point = it.point
            touch.type = TouchType.Ended
            touchables.filter { it.enable }
                    .forEach { it.on(touch) }
        }
        touchEvents.forEach { touches.remove(it) }
        if (touches.isEmpty()) {
            incremental = 0
        }
    }

    override fun onTouchesCancelled() {
        touchables.forEach { it.on(TouchEntity(-1, Vector2(), TouchType.Cancelled)) }
    }

    override fun add(touchable: Touchable) {
        touchables.add(touchable)
    }
}