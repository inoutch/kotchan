package kotchan.controller.touchable

import kotchan.controller.TouchType
import utility.type.Vector2

open class CircleTouchable(
        val center: Vector2,
        val radius: Float,
        callback: (point: Vector2, type: TouchType) -> Boolean) : Touchable(callback) {
    override fun check(point: Vector2): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}