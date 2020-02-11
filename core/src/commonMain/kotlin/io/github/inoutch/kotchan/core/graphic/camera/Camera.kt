package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.useVulkan
import io.github.inoutch.kotchan.math.Matrix3F
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

abstract class Camera {
    companion object {
        fun createOrthographic(
            left: Float,
            right: Float,
            bottom: Float,
            top: Float,
            near: Float,
            far: Float
        ): Matrix4F {
            return if (useVulkan) {
                // for vulkan [z:0.0 ~ 1.0]
                val m3 = Matrix3F.createDiagonal(
                        2 / (right - left),
                        2 / (top - bottom),
                        -1 / (far - near))

                val tx = -(right + left) / (right - left)
                val ty = -(top + bottom) / (top - bottom)
                val tz = -(near) / (far - near)
                Matrix4F(m3, Vector4F(tx, ty, tz, 1.0f))
            } else {
                // for opengl [z:-1.0 ~ 1.0]
                val m3 = Matrix3F.createDiagonal(
                        2 / (right - left),
                        2 / (top - bottom),
                        -2 / (far - near))

                val tx = -(right + left) / (right - left)
                val ty = -(top + bottom) / (top - bottom)
                val tz = -(far + near) / (far - near)
                Matrix4F(m3, Vector4F(tx, ty, tz, 1.0f))
            }
        }
    }

    var projectionMatrix = Matrix4F()

    var viewMatrix = Matrix4F()

    var position = Vector3F(0.0f, 0.0f, 1.0f)

    var combine = projectionMatrix * viewMatrix
        private set

    open fun update() {
        combine = projectionMatrix * viewMatrix
    }
}
