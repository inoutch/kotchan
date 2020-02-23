package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.core.KotchanGlobalContext
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotchan.math.Vector3F

class Camera2D private constructor() : Camera() {
    companion object {
        fun create(): Camera2D {
            return Camera2D().apply {
                projectionMatrix = createOrthographic(
                        0.0f,
                        KotchanGlobalContext.config.screenSize.x.toFloat(),
                        0.0f,
                        KotchanGlobalContext.config.screenSize.y.toFloat(),
                        -10000.0f,
                        10000.0f
                )
                update()
            }
        }
    }

    var position = Vector3F.Zero

    override fun update() {
        viewMatrix = Matrix4F.createTranslation(position)
        super.update()
    }
}
