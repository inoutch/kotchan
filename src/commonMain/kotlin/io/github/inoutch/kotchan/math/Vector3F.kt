package io.github.inoutch.kotchan.math

import kotlin.math.sqrt
import kotlinx.serialization.Serializable

@Serializable
data class Vector3F(var x: Float, var y: Float, var z: Float) {
    companion object {
        val Zero: Vector3F
            get() = Vector3F(0.0f, 0.0f, 0.0f)
    }

    private constructor() : this(0.0f, 0.0f, 0.0f)

    constructor(v: Float) : this(v, v, v)

    constructor(vector2: Vector2F, z: Float) : this(vector2.x, vector2.y, z)

    constructor(vector4: Vector4F) : this(vector4.x, vector4.y, vector4.z)

    fun length() = sqrt(x * x + y * y + z * z)

    fun normalized(): Vector3F {
        val len = length()
        return Vector3F(x / len, y / len, z / len)
    }

    fun copyCoordinatesTo(arr: MutableList<Float>) {
        arr.add(x)
        arr.add(y)
        arr.add(z)
    }

    fun dotProduct(other: Vector3F) = x * other.x + y * other.y + z * other.z

    fun crossProduct(other: Vector3F): Vector3F =
            Vector3F(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

    operator fun minus(other: Vector3F) = Vector3F(x - other.x, y - other.y, z - other.z)

    operator fun plus(other: Vector3F) = Vector3F(x + other.x, y + other.y, z + other.z)

    operator fun times(other: Float) = Vector3F(x * other, y * other, z * other)

    operator fun times(other: Vector3F) = Vector3F(x * other.x, y * other.y, z * other.z)

    operator fun div(other: Vector3F) = Vector3F(x / other.x, y / other.y, z / other.z)

    operator fun div(other: Float) = Vector3F(x / other, y / other, z / other)
}

fun List<Vector3F>.flatten() = FloatArray(this.size * 3).also {
    this.forEachIndexed { index, vector ->
        it[index * 3 + 0] = vector.x
        it[index * 3 + 1] = vector.y
        it[index * 3 + 2] = vector.z
    }
}
