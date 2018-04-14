package kotchan.controller.touchable

import interop.graphic.GLCamera
import kotchan.controller.TouchType
import utility.type.Rect
import utility.type.Vector2

open class RectTouchable(
        private val rect: () -> Rect,
        camera: GLCamera,
        callback: (point: Vector2, type: TouchType, check: Boolean) -> Unit) : Touchable(camera, callback) {
    override fun check(point: Vector2, camera: GLCamera): Boolean {
        val rect = rect()
        val p1 = camera.combine * rect.origin
        val p2 = camera.combine * (rect.origin + rect.size)
        if (p1.x <= point.x &&
                p1.y <= point.y &&
                p2.x > point.x &&
                p2.y > point.y) {
            return true
        }
        return false
    }
}