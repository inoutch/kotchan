package io.github.inoutch.kotchan.core.controller.touch.listener.decision

import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector2

interface TouchDecision {

    fun check(point: Point, normalizedPoint: Vector2, camera: Camera): Boolean
}
