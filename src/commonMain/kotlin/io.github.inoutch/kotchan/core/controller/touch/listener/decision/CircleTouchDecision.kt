package io.github.inoutch.kotchan.core.controller.touch.listener.decision

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.utility.type.Circle
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector2

class CircleTouchDecision(private val circle: () -> Circle) : TouchDecision {
    override fun check(point: Point, normalizedPoint: Vector2, camera: Camera): Boolean {
        val circle = circle()
        val p1 = (circle.center - Vector2(camera.position))
        val p2 = KotchanCore.instance.screenSize * (normalizedPoint + 1.0f) / 2.0f
        return Circle.collision(Circle(p1, circle.radius), Circle(p2, 0.0f))
    }
}