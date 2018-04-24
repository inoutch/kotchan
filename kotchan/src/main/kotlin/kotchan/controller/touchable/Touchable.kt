package kotchan.controller.touchable

import interop.graphic.GLCamera
import kotchan.controller.*
import utility.type.*

abstract class Touchable(private val camera: GLCamera) {
    var priority: Int = 0
    var enable = true
    abstract fun check(point: Vector2, camera: GLCamera): Boolean
    abstract fun callback(index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean

    fun on(touch: Touch, chain: Boolean): Boolean {
        val normal = TouchControllerEntity.convertNormalPointInView(touch.point())
        return callback(touch.index(), normal, touch.type(), check(normal, camera), chain)
    }
}