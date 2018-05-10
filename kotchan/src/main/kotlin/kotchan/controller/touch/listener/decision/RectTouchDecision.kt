package kotchan.controller.touch.listener.decision

import kotchan.view.camera.Camera
import utility.type.Rect
import utility.type.Vector2

class RectTouchDecision(private val rect: () -> Rect) : TouchDecision {
    override fun check(point: Vector2, normalizedPoint: Vector2, camera: Camera): Boolean {
        val rect = rect()
        val p1 = camera.combine * rect.origin
        val p2 = camera.combine * (rect.origin + rect.size)
        if (p1.x <= normalizedPoint.x &&
                p1.y <= normalizedPoint.y &&
                p2.x > normalizedPoint.x &&
                p2.y > normalizedPoint.y) {
            return true
        }
        return false
    }
}