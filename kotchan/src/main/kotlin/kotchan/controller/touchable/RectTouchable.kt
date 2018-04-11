package kotchan.controller.touchable

import kotchan.controller.TouchType
import utility.type.Rect
import utility.type.Vector2

open class RectTouchable(var rect: Rect, callback: (point: Vector2, type: TouchType) -> Boolean) : Touchable(callback) {
    override fun check(point: Vector2): Boolean {
        println(rect)
        println(point)
        if (rect.origin.x >= point.x &&
                rect.origin.y >= point.y &&
                rect.origin.x + rect.size.x < point.x &&
                rect.origin.y + rect.size.y < point.y) {
            return true
        }
        return false
    }
}