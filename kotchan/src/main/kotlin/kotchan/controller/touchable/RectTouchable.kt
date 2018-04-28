package kotchan.controller.touchable

import kotchan.camera.Camera
import kotchan.controller.TouchType
import utility.type.Rect
import utility.type.Vector2

open class RectTouchable(
        private val rect: () -> Rect,
        camera: Camera,
        private val argCallback: ((index: Int, point: Vector2, type: TouchType, check: Boolean, skip: Boolean) -> Boolean)?) : Touchable(camera) {
    override fun callback(index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        return argCallback?.invoke(index, point, type, check, chain) ?: true
    }

    override fun check(point: Vector2, camera: Camera): Boolean {
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