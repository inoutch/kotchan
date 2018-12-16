package io.github.inoutch.kotchan.core.controller.touch.listener

import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.core.controller.touch.Touch
import io.github.inoutch.kotchan.core.controller.touch.TouchControllerEntity
import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.toPoint
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.TouchDecision

abstract class TouchListener(private val camera: Camera) {

    var decision: TouchDecision? = null

    var priority: Int = 0

    var enable = true

    abstract fun callback(index: Int, point: Point, type: TouchType, check: Boolean, chain: Boolean): Boolean

    fun on(touch: Touch, chain: Boolean): Boolean {
        val normalized = TouchControllerEntity.convertNormalPointInView(touch.point().toVector2())
        val check = decision?.check(touch.point(), normalized, camera) ?: true
        return callback(touch.index(), normalized.toPoint(), touch.type(), check, chain)
    }

    open fun update(delta: Float) {}
}