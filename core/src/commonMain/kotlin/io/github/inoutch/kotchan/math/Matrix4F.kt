package io.github.inoutch.kotchan.math

import kotlin.math.abs
import kotlinx.serialization.Serializable

@Serializable
data class Matrix4F(val col1: Vector4F, val col2: Vector4F, val col3: Vector4F, val col4: Vector4F) {
    companion object {
        fun createTranslation(vector: Vector3F) = Matrix4F(
                Matrix3F(),
                Vector4F(vector.x, vector.y, vector.z, 1.0f))

        fun createScale(vector: Vector3F) = Matrix4F(
                Vector4F(vector.x, 0.0f, 0.0f, 0.0f),
                Vector4F(0.0f, vector.y, 0.0f, 0.0f),
                Vector4F(0.0f, 0.0f, vector.z, 0.0f),
                Vector4F(0.0f, 0.0f, 0.0f, 1.0f))
    }

    constructor() : this(
            Vector4F(1.0f, 0.0f, 0.0f, 0.0f),
            Vector4F(0.0f, 1.0f, 0.0f, 0.0f),
            Vector4F(0.0f, 0.0f, 1.0f, 0.0f),
            Vector4F(0.0f, 0.0f, 0.0f, 1.0f))

    constructor(Matrix3F: Matrix3F, col4: Vector4F = Vector4F(0.0f, 0.0f, 0.0f, 1.0f)) : this(
            col1 = Vector4F(Matrix3F.col1, 0.0f),
            col2 = Vector4F(Matrix3F.col2, 0.0f),
            col3 = Vector4F(Matrix3F.col3, 0.0f),
            col4 = col4
    )

    fun flatten(): FloatArray {
        val values = mutableListOf<Float>()
        col1.copyCoordinatesTo(values)
        col2.copyCoordinatesTo(values)
        col3.copyCoordinatesTo(values)
        col4.copyCoordinatesTo(values)
        return values.toFloatArray()
    }

    fun inverse(): Matrix4F {
        val a0 = col1.x * col2.y - col1.y * col2.x
        val a1 = col1.x * col2.z - col1.z * col2.x
        val a2 = col1.x * col2.w - col1.w * col2.x
        val a3 = col1.y * col2.z - col1.z * col2.y
        val a4 = col1.y * col2.w - col1.w * col2.y
        val a5 = col1.z * col2.w - col1.w * col2.z
        val b0 = col3.x * col4.y - col3.y * col4.x
        val b1 = col3.x * col4.z - col3.z * col4.x
        val b2 = col3.x * col4.w - col3.w * col4.x
        val b3 = col3.y * col4.z - col3.z * col4.y
        val b4 = col3.y * col4.w - col3.w * col4.y
        val b5 = col3.z * col4.w - col3.w * col4.z

        val det = a0 * b5 - a1 * b4 + a2 * b3 + a3 * b2 - a4 * b1 + a5 * b0
        if (abs(det) <= 2e-37f)
            throw Error("could not inverse")

        val col1 = Vector4F(
                col2.y * b5 - col2.z * b4 + col2.w * b3,
                -col1.y * b5 + col1.z * b4 - col1.w * b3,
                col4.y * a5 - col4.z * a4 + col4.w * a3,
                -col3.y * a5 + col3.z * a4 - col3.w * a3)
        val col2 = Vector4F(
                -col2.x * b5 + col2.z * b2 - col2.w * b1,
                col1.x * b5 - col1.z * b2 + col1.w * b1,
                -col4.x * a5 + col4.z * a2 - col4.w * a1,
                col3.x * a5 - col3.z * a2 + col3.w * a1)
        val col3 = Vector4F(
                col2.x * b4 - col2.y * b2 + col2.w * b0,
                -col1.x * b4 + col1.y * b2 - col1.w * b0,
                col4.x * a4 - col4.y * a2 + col4.w * a0,
                -col3.x * a4 + col3.y * a2 - col3.w * a0)
        val col4 = Vector4F(
                -col2.x * b3 + col2.y * b1 - col2.z * b0,
                col1.x * b3 - col1.y * b1 + col1.z * b0,
                -col4.x * a3 + col4.y * a1 - col4.z * a0,
                col3.x * a3 - col3.y * a1 + col3.z * a0
        )
        return Matrix4F(col1, col2, col3, col4) * (1.0f / det)
    }

    operator fun times(other: Vector4F) = Vector4F(
            col1.x * other.x + col2.x * other.y + col3.x * other.z + col4.x * other.w,
            col1.y * other.x + col2.y * other.y + col3.y * other.z + col4.y * other.w,
            col1.z * other.x + col2.z * other.y + col3.z * other.z + col4.z * other.w,
            col1.w * other.x + col2.w * other.y + col3.w * other.z + col4.w * other.w
    )

    operator fun times(other: Vector3F) = this * Vector4F(other, 1.0f)

    operator fun times(other: Vector2F) = this * Vector3F(other, 1.0f)

    operator fun times(other: Matrix4F) = Matrix4F(
            col1 = this * other.col1,
            col2 = this * other.col2,
            col3 = this * other.col3,
            col4 = this * other.col4
    )

    operator fun times(other: Float) = Matrix4F(
            col1 * other,
            col2 * other,
            col3 * other,
            col4 * other)
}
