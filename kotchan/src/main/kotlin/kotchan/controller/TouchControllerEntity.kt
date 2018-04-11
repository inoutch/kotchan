package kotchan.controller

import interop.graphic.GLCamera
import kotchan.Engine
import kotchan.controller.touchable.Touchable
import utility.type.Vector2
import utility.type.Vector4

class TouchControllerEntity : TouchEmitter, TouchController {
    // about touchable
    private val touchables: MutableList<Touchable> = mutableListOf()
    private val targets: MutableMap<TouchEntity, Touchable> = mutableMapOf()

    // about touch
    private val touches: MutableMap<TouchEvent, TouchEntity> = mutableMapOf()

    private val touchesByOneCycle: MutableMap<Int, TouchEntity> = mutableMapOf()
    private val touchesByOneCycleInv: MutableMap<TouchEntity, Int> = mutableMapOf()
    private var increasingCount = 0

    private val willDestroyTouchEvents: MutableList<TouchEvent> = mutableListOf()

    private var camera: GLCamera = GLCamera()

    private fun convertPoint(p: Vector2): Vector2 {
        val p4 = camera.combine * Vector4(p.x, p.y, 0.0f, 1.0f)
        return Vector2(p4.x, p4.y)
    }

    override fun onTouchesBegan(touchEvents: List<TouchEvent>) {
        touchEvents.forEach { event ->
            val touch = TouchEntity(event.point, TouchType.Began)
            touches[event] = touch
            touchables
                    .filter { it.check(event.point / Engine.getInstance().windowSize) }
                    //.filter { it.check(convertPoint(event.point)) }
                    .forEach {
                        targets[touch] = it
                        it.on(event.point, TouchType.Began)
                    }
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
            targets[touch]?.on(touch.point, TouchType.Moved)
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
            targets[touch]?.on(touch.point, TouchType.Moved)
            targets.remove(touch)
        }
        willDestroyTouchEvents.addAll(touchEvents)
    }

    override fun onTouchesCancelled() {
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

    override fun camera(camera: GLCamera) {
        this.camera = camera
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
            targets.remove(touch)

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