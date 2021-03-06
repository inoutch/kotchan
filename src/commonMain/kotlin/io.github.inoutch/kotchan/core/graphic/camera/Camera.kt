package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.utility.type.*

abstract class Camera {
    companion object {
        fun createOrthographic(left: Float, right: Float,
                               bottom: Float, top: Float,
                               near: Float, far: Float): Matrix4 {
            return if (instance.vk != null) {
                // for vulkan [z:0.0 ~ 1.0]
                val m3 = Matrix3.createDiagonal(
                        2 / (right - left),
                        2 / (top - bottom),
                        -1 / (far - near))

                val tx = -(right + left) / (right - left)
                val ty = -(top + bottom) / (top - bottom)
                val tz = -(near) / (far - near)
                Matrix4(m3, Vector4(tx, ty, tz, 1.0f))
            } else {
                // for opengl [z:-1.0 ~ 1.0]
                val m3 = Matrix3.createDiagonal(
                        2 / (right - left),
                        2 / (top - bottom),
                        -2 / (far - near))

                val tx = -(right + left) / (right - left)
                val ty = -(top + bottom) / (top - bottom)
                val tz = -(far + near) / (far - near)
                Matrix4(m3, Vector4(tx, ty, tz, 1.0f))
            }
        }
    }

    var projectionMatrix = Matrix4()

    var viewMatrix = Matrix4()

    var position = Vector3(0.0f, 0.0f, 1.0f)

    var combine = projectionMatrix * viewMatrix
        private set

    open fun update() {
        combine = projectionMatrix * viewMatrix
    }
}
