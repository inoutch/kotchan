package kotchan.controller.touchable

import interop.graphic.GLCamera
import kotchan.controller.*
import utility.type.*

abstract class Touchable(private val camera: GLCamera, private val callback: (index: Int, point: Vector2, type: TouchType, check: Boolean) -> Unit) {
    var type = TouchableType.EventHandler
    abstract fun check(point: Vector2, camera: GLCamera): Boolean

    fun on(touch: Touch) {
        val normal = TouchControllerEntity.convertNormalPointInView(touch.point())
        callback(touch.index(), normal, touch.type(), check(normal, camera))
    }
}