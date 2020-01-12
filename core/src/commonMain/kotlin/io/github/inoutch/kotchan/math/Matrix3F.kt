package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
data class Matrix3F(val col1: Vector3F, val col2: Vector3F, val col3: Vector3F) {
    companion object {
        fun createDiagonal(d1: Float, d2: Float, d3: Float) = Matrix3F(
                col1 = Vector3F(d1, 0.0f, 0.0f),
                col2 = Vector3F(0.0f, d2, 0.0f),
                col3 = Vector3F(0.0f, 0.0f, d3)
        )
    }

    constructor() : this(
            Vector3F(1.0f, 0.0f, 0.0f),
            Vector3F(0.0f, 1.0f, 0.0f),
            Vector3F(0.0f, 0.0f, 1.0f))

    operator fun times(other: Vector3F) = Vector3F(
            col1.x * other.x + col2.x * other.y + col3.x * other.z,
            col1.y * other.x + col2.y * other.y + col3.y * other.z,
            col1.z * other.x + col2.z * other.y + col3.z * other.z)

    operator fun times(other: Matrix3F) = Matrix3F(
            col1 = this * other.col1,
            col2 = this * other.col2,
            col3 = this * other.col3)
}
