package kotchan.controller.touch.listener.decision

import kotchan.Engine
import kotchan.view.camera.Camera
import utility.type.Circle
import utility.type.Vector2

class CircleTouchDecision(private val circle: () -> Circle) : TouchDecision {
    override fun check(point: Vector2, normalizedPoint: Vector2, camera: Camera): Boolean {
        val circle = circle()
        val p1 = (circle.center - Vector2(camera.position))
        val p2 = Engine.getInstance().screenSize * (normalizedPoint + 1.0f) / 2.0f
        return Circle.collision(Circle(p1, circle.radius), Circle(p2, 0.0f))
    }
}