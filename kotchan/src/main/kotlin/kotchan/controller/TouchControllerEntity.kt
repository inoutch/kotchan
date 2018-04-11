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

    private val touchesByOneCycle: MutableMap<Int, TouchEntity> = mutableMapOf()
    private val touchesByOneCycleInv: MutableMap<TouchEntity, Int> = mutableMapOf()
    private var increasingCount = 0

    private val willDestroyTouchEvents: MutableList<TouchEvent> = mutableListOf()

    override fun onTouchesBegan(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = TouchEntity(it.point, TouchType.Began)
            touches[it] = touch
            touchables.forEach { it.on(touch.point, TouchType.Began) }
        }
    }

    override fun onTouchesMoved(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return@forEach
            touch.point = it.point
            touch.type = when (touch.type) {
                TouchType.Began -> TouchType.BeganAndMoved
                else -> TouchType.Moved
            }
            touchables.forEach { it.on(touch.point, TouchType.Began) }
        }
    }

    override fun onTouchesEnded(touchEvents: List<TouchEvent>) {
        touchEvents.forEach {
            val touch = touches[it] ?: return@forEach
            touch.point = it.point
            touch.type = when (touch.type) {
                TouchType.Began -> TouchType.BeganAndEnded
                TouchType.BeganAndMoved -> TouchType.BeganAndMovedAndEnded
                else -> TouchType.Ended
            }
            touchables.forEach { it.on(touch.point, touch.type) }
        }
        willDestroyTouchEvents.addAll(touchEvents)
    }

    override fun onTouchesCancelled() {
        touchables.forEach { it.on(Vector2(), TouchType.Cancelled) }
        willDestroyTouchEvents.clear()
        willDestroyTouchEvents.addAll(touches.keys)
        touchesByOneCycle.clear()
        touchesByOneCycleInv.clear()
    }

    override fun touches(): List<Touch> = touches.values.toList()

    override fun touchesByOneCycle(index: Int): Touch? = touchesByOneCycle[index]

    override fun add(touchable: Touchable) {
        touchables.add(touchable)
    }

    fun begin() {
        touches.forEach {
            if (touchesByOneCycleInv[it.value] != null) {
                return@forEach
            }
            when (it.value.type) {
                TouchType.Began,
                TouchType.BeganAndMoved,
                TouchType.BeganAndEnded,
                TouchType.BeganAndMovedAndEnded -> {
                    touchesByOneCycle[increasingCount] = it.value
                    touchesByOneCycleInv[it.value] = increasingCount
                    increasingCount++
                }
                else -> {
                }
            }
        }
    }

    fun end() {
        touches.forEach {
            it.value.type = when (it.value.type) {
                TouchType.Began -> TouchType.BeganIdle
                TouchType.Moved -> TouchType.MovedIdle
                TouchType.BeganAndMoved -> TouchType.MovedIdle
                else -> it.value.type
            }
        }

        willDestroyTouchEvents.forEach {
            val touch = touches[it]
            touches.remove(it)

            val i = touchesByOneCycleInv[touch]
            touchesByOneCycleInv.remove(touch)
            touchesByOneCycle.remove(i)
        }
        willDestroyTouchEvents.clear()

        if (touches.isEmpty()) {
            increasingCount = 0
        }
    }
}