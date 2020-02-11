package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.core.KotchanGlobalContext

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
            }
        }
    }
}