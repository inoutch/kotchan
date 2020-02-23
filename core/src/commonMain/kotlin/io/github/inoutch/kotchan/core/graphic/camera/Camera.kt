package io.github.inoutch.kotchan.core.graphic.camera

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.useVulkan
import io.github.inoutch.kotchan.math.Matrix3F
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F
import kotlin.math.tan

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
                        1 / (far - near))

                val tx = -(right + left) / (right - left)
                val ty = -(top + bottom) / (top - bottom)
                val tz = -(near) / (far - near)
                Matrix4F(m3, Vector4F(tx, ty, tz, 1.0f))
            } else {
                // for opengl [z:-1.0 ~ 1.0]
                val m3 = Matrix3F.createDiagonal(
                        2 / (right - left),
                        2 / (top - bottom),
                        2 / (far - near))

                val tx = -(right + left) / (right - left)
                val ty = -(top + bottom) / (top - bottom)
                val tz = -(far + near) / (far - near)
                Matrix4F(m3, Vector4F(tx, ty, tz, 1.0f))
            }
        }

        fun createPerspective(fieldOfViewRadian: Float, aspectRatio: Float, zNearPlane: Float, zFarPlane: Float): Matrix4F {
            val fn = 1.0f / (zFarPlane - zNearPlane)
            val divisor = tan(fieldOfViewRadian)
            val factor = 1.0f / divisor

            val m10: Float
            val m14: Float
            if (useVulkan) {
                m10 = -zFarPlane * fn
                m14 = -zFarPlane * zNearPlane * fn
            } else {
                m10 = -(zFarPlane + zNearPlane) * fn
                m14 = -2.0f * zFarPlane * zNearPlane * fn
            }
            return Matrix4F(
                    Vector4F((1.0f / aspectRatio) * factor, 0.0f, 0.0f, 0.0f),
                    Vector4F(0.0f, -factor, 0.0f, 0.0f),
                    Vector4F(0.0f, 0.0f, m10, -1.0f),
                    Vector4F(0.0f, 0.0f, m14, 0.0f)
            )
        }

        fun createLookAt(
                eyePosition: Vector3F,
                target: Vector3F,
                up: Vector3F
        ): Matrix4F {
            val nUp = up.normalized()
            val zAxis = (eyePosition - target).normalized()
            val xAxis = nUp.crossProduct(zAxis).normalized()
            val yAxis = zAxis.crossProduct(xAxis).normalized()

            return Matrix4F(
                    Vector4F(xAxis.x, yAxis.x, zAxis.x, 0.0f),
                    Vector4F(xAxis.y, yAxis.y, zAxis.y, 0.0f),
                    Vector4F(xAxis.z, yAxis.z, zAxis.z, 0.0f),
                    Vector4F(
                            -xAxis.dotProduct(eyePosition),
                            -yAxis.dotProduct(eyePosition),
                            -zAxis.dotProduct(eyePosition),
                            1.0f
                    )
            )
        }
    }

    var projectionMatrix = Matrix4F()

    var viewMatrix = Matrix4F()

    var combine = projectionMatrix * viewMatrix
        private set

    open fun update() {
        combine = projectionMatrix * viewMatrix
    }
}
