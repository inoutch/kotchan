package interop.graphic

import utility.type.Matrix3
import utility.type.Matrix4
import utility.type.Vector4

class GLCamera {
    companion object {
        fun createOrthographic(left: Float, right: Float,
                               bottom: Float, top: Float,
                               near: Float, far: Float) = GLCamera().apply {
            val m3 = Matrix3.createDiagonal(
                    2 / (right - left),
                    2 / (top - bottom),
                    -2 / (far - near))

            val tx = -(right + left) / (right - left)
            val ty = -(top + bottom) / (top - bottom)
            val tz = -(far + near) / (far - near)

            projectionMatrix = Matrix4(m3, Vector4(tx, ty, tz, 1.0f))
        }
    }

    var projectionMatrix = Matrix4()
    var viewMatrix = Matrix4()

    var combine = projectionMatrix * viewMatrix
        private set
    var inverse = combine.inverse()

    fun update() {
        combine = projectionMatrix * viewMatrix
        inverse = combine.inverse()
    }
}