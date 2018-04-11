package kotchan.controller.touchable

import kotchan.controller.*
import utility.type.*

abstract class Touchable(private val callback: (point: Vector2, type: TouchType) -> Boolean) {
    var type: TouchType = TouchType.None
    abstract fun check(point: Vector2): Boolean

    fun on(point: Vector2, type: TouchType) {
        callback(point, type)
    }
}