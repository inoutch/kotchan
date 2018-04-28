package kotchan.controller.touchable

import kotchan.camera.Camera
import kotchan.controller.*
import utility.type.*

abstract class Touchable(private val camera: Camera) {
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