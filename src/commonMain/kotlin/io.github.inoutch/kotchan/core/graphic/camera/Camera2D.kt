package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.utility.type.Matrix4

class Camera2D : Camera() {
    override fun update() {
        viewMatrix = Matrix4.createTranslation(position * -1.0f)
        super.update()
    }
}
