package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Vector2

class Camera2D : Camera() {
    companion object {
        fun createCenteredCamera2D(screenSize: Vector2 = core.screenSize.toVector2()): Camera2D {
            return Camera2D().apply {
                projectionMatrix = createOrthographic(
                        -screenSize.x / 2.0f,
                        screenSize.x / 2.0f,
                        -screenSize.y / 2.0f,
                        screenSize.y / 2.0f,
                        -1000.0f,
                        1000.0f)
                update()
            }
        }
    }

    override fun update() {
        viewMatrix = Matrix4.createTranslation(position * -1.0f)
        super.update()
    }
}
