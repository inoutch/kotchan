package kotchan.controller.touch.listener

import kotchan.view.camera.Camera
import kotchan.controller.touch.Touch
import kotchan.controller.touch.TouchControllerEntity
import kotchan.controller.touch.TouchType
import kotchan.controller.touch.listener.decision.TouchDecision
import utility.type.*

abstract class TouchListener(private val camera: Camera) {
    var decision: TouchDecision? = null
    var priority: Int = 0
    var enable = true
    abstract fun callback(index: Int, point: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean

    fun on(touch: Touch, chain: Boolean): Boolean {
        val normalized = TouchControllerEntity.convertNormalPointInView(touch.point())
        val check = decision?.check(touch.point(), normalized, camera) ?: true
        return callback(touch.index(), normalized, touch.type(), check, chain)
    }

    open fun update(delta: Float) {}
}