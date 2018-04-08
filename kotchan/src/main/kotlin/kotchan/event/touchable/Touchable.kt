package kotchan.event.touchable

import kotchan.event.*
import utility.type.*

abstract class Touchable(private val callback: (point: Vector2, type: TouchType) -> Boolean) {
    var index = 0
    var type: TouchType = TouchType.None
    abstract fun check(point: Vector2): Boolean

    fun on(point: Vector2, type: TouchType) {
        callback(point, type)
    }
}