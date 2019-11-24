package io.github.inoutch.kotchan.core.controller.touch.listener

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.utility.collection.FixedStack
import io.github.inoutch.kotchan.utility.type.Vector2
import kotlin.math.abs

open class ScrollTouchListener(
    camera: Camera,
    private val setVelocity: (velocity: Vector2) -> Unit
) : TouchListener(camera) {
    // settings
    var accelerationEnable = false

    private var before: Vector2? = null

    private var acceleration: Vector2 = Vector2()

    private var pointHistory = FixedStack<Vector2>(8)

    override fun callback(index: Int, normalizedPoint: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        if (index != 0) {
            // only single touch
            return true
        }
        // point is -1.0 ~ 1.0
        val after = normalizedPoint * core.screenSize / 2.0f
        if (type == TouchType.Began && chain) {
            before = after
            return false
        }
        val checkedBefore = before
        if (checkedBefore != null) {
            before = when (type) {
                TouchType.Moved -> {
                    moved(checkedBefore, after)
                    after
                }
                TouchType.Ended -> {
                    end(checkedBefore, after)
                    null
                }
                else -> null
            }
            return false
        }
        return chain
    }

    override fun update(delta: Float) {
        if (before != null || !accelerationEnable) {
            return
        }
        if (acceleration.length() > 0) {
            setVelocity(acceleration * delta * 60.0f)
            val friction = acceleration.normalized() * delta * 8.0f
            if (abs(friction.x) > abs(acceleration.x) || abs(friction.y) > abs(acceleration.y)) {
                acceleration = Vector2.Zero
            } else {
                acceleration -= friction
            }
        }
    }

    private fun moved(before: Vector2, after: Vector2) {
        val d = after - before
        setVelocity(d)
        pointHistory.push(d)
    }

    private fun end(before: Vector2, after: Vector2) {
        val d = after - before
        if (!accelerationEnable) {
            setVelocity(d)
        }
        if (!pointHistory.isEmpty()) {
            acceleration = pointHistory.reduce { x, y -> x + y } / pointHistory.size.toFloat()
            pointHistory.clear()
        }
    }
}
