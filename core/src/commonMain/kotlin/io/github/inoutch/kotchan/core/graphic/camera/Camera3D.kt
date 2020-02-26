package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.math.Vector3F
import kotlin.math.PI

class Camera3D private constructor() : Camera() {
    companion object {
        fun create(fieldOfView: Float = 45.0f, zNearPlane: Float = 0.1f, zFarPlane: Float = 10.0f): Camera3D {
            return Camera3D().apply {
                val aspect = config.screenSize.x.toFloat() / config.screenSize.y.toFloat()
                projectionMatrix = createPerspective(
                        fieldOfView / 180.0f * PI.toFloat(),
                        aspect,
                        zNearPlane,
                        zFarPlane
                )
                update()
            }
        }
    }

    var position = Vector3F(0.0f, 0.0f, 0.0f)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            isChanged = true
        }

    var targetPosition = Vector3F.Zero
        set(value) {
            if (field == value) {
                return
            }
            field = value
            isChanged = true
        }

    var up = Vector3F(0.0f, 0.0f, 1.0f)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            isChanged = true
        }

    private var isChanged = true

    override fun update() {
        if (isChanged) {
            viewMatrix = createLookAt(position, targetPosition, up)
            isChanged = false
        }

        super.update()
    }
}
