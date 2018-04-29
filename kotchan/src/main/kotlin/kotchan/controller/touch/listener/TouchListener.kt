package kotchan.controller.touch.listener

import kotchan.view.camera.Camera
import kotchan.controller.touch.Touch
import kotchan.controller.touch.TouchControllerEntity
import kotchan.controller.touch.TouchType
import utility.type.*

abstract class TouchListener(private val camera: Camera) {
    var priority: Int = 0
    var enable = true
    abstract fun check(point: Vector2, camera: Camera): Boolean
    abstract fun callback(index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean

    fun on(touch: Touch, chain: Boolean): Boolean {
        val normal = TouchControllerEntity.convertNormalPointInView(touch.point())
        return callback(touch.index(), normal, touch.type(), check(normal, camera), chain)
    }

    open fun update(delta: Float) {}
}