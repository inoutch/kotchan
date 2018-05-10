package kotchan.controller.touch.listener.decision

import kotchan.view.camera.Camera
import utility.type.Vector2

interface TouchDecision {
    fun check(point: Vector2, normalizedPoint: Vector2, camera: Camera): Boolean
}