package io.github.inoutch.kotchan.core.view.camera

import io.github.inoutch.kotchan.utility.type.*

class Camera3D : Camera() {

    private var lookAt = Vector3()

    private var up = Vector3(0.0f, 1.0f, 0.0f)

    companion object {
        fun createLookAt(eyePosition: Vector3, targetPosition: Vector3, up: Vector3): Matrix4 {
            val zaxis = (eyePosition - targetPosition).normalized()
            val xaxis = (up * zaxis).normalized()
            val yaxis = (zaxis * xaxis).normalized()

            val mat3 = Matrix3(
                    Vector3(xaxis.x, yaxis.x, zaxis.x),
                    Vector3(xaxis.y, yaxis.y, zaxis.y),
                    Vector3(xaxis.z, yaxis.z, zaxis.z))
            val vec4 = Vector4(
                    -xaxis.dotProduct(eyePosition),
                    -yaxis.dotProduct(eyePosition),
                    -zaxis.dotProduct(eyePosition), 1.0f)
            return Matrix4(mat3, vec4)
        }
    }

    override fun update() {
        viewMatrix = createLookAt(position, lookAt, up)
        super.update()
    }
}