package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable
import kotlin.math.sqrt

@Serializable
data class Vector2(val x: Float, val y: Float) {
    constructor() : this(0.0f, 0.0f)
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
    constructor(vector: Vector3) : this(vector.x, vector.y)

    fun length() = sqrt(x * x + y * y)

    fun normalized(): Vector2 {
        val len = length()
        return Vector2(x / len, y / len)
    }

    fun copyCoordinatesTo(arr: MutableList<Float>) {
        arr.add(x)
        arr.add(y)
    }

    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
    operator fun minus(other: Float) = Vector2(x - other, y - other)
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun plus(other: Float) = Vector2(x + other, y + other)
    operator fun plus(other: Point) = Vector2(x + other.x, y + other.y)
    operator fun times(other: Float) = Vector2(x * other, y * other)
    operator fun times(other: Vector2) = Vector2(x * other.x, y * other.y)
    operator fun times(other: Point) = Vector2(x * other.x, y * other.y)
    operator fun div(other: Float) = Vector2(x / other, y / other)
    operator fun div(other: Vector2) = Vector2(x / other.x, y / other.y)
    operator fun div(other: Point) = Vector2(x / other.x, y / other.y)

    fun toVector3(z: Float = 0.0f) = Vector3(x, y, z)
    fun toVector4(z: Float = 0.0f, w: Float = 0.0f) = Vector4(x, y, z, w)

    companion object {
        val Zero = Vector2(0.0f, 0.0f)
    }
}

fun List<Vector2>.flatten() = FloatArray(this.size * 2).also {
    this.forEachIndexed { index, vector ->
        it[index * 2 + 0] = vector.x
        it[index * 2 + 1] = vector.y
    }
}

fun List<Float>.toVector2(offset: Int = 0) =
        if (this.size >= 2 + offset) Vector2(this[offset], this[offset + 1]) else Vector2()

