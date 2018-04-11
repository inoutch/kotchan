package kotchan.controller.touchable

import interop.graphic.GLCamera
import kotchan.controller.*
import utility.type.*

abstract class Touchable(private val camera: GLCamera, private val callback: (point: Vector2, type: TouchType, check: Boolean) -> Unit) {
    var type: TouchType = TouchType.None
    abstract fun check(point: Vector2, camera: GLCamera): Boolean

    fun on(point: Vector2, type: TouchType) {
        callback(point, type, check(TouchControllerEntity.convertNormalPointInView(point), camera))
    }
}