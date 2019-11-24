package io.github.inoutch.kotchan.core.math

import io.github.inoutch.kotchan.core.extension.fastForEachIndexed
import kotlin.math.sqrt
import kotlinx.serialization.Serializable

@Serializable
data class Vector2F(var x: Float, var y: Float) {
    companion object {
        val Zero: Vector2F
            get() = Vector2F()
    }

    private constructor() : this(0.0f, 0.0f)

    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())

    constructor(vector: Vector3F) : this(vector.x, vector.y)

    fun length() = sqrt(x * x + y * y)

    fun normalized(): Vector2F {
        val len = length()
        return Vector2F(x / len, y / len)
    }

    fun copyCoordinatesTo(arr: MutableList<Float>) {
        arr.add(x)
        arr.add(y)
    }

    fun dotProduct(other: Vector2F) = x * other.x + y * other.y

    fun inRange(p: Vector2F) = 0 <= p.x && 0 <= p.y && p.x < x && p.y < y

    operator fun minus(other: Vector2F) = Vector2F(x - other.x, y - other.y)

    operator fun minus(other: Float) = Vector2F(x - other, y - other)

    operator fun plus(other: Vector2F) = Vector2F(x + other.x, y + other.y)

    operator fun plus(other: Float) = Vector2F(x + other, y + other)

    operator fun plus(other: Vector2I) = Vector2F(x + other.x, y + other.y)

    operator fun times(other: Float) = Vector2F(x * other, y * other)

    operator fun times(other: Vector2F) = Vector2F(x * other.x, y * other.y)

    operator fun times(other: Vector2I) = Vector2F(x * other.x, y * other.y)

    operator fun div(other: Float) = Vector2F(x / other, y / other)

    operator fun div(other: Vector2F) = Vector2F(x / other.x, y / other.y)

    operator fun div(other: Vector2I) = Vector2F(x / other.x, y / other.y)

    fun toVector3F(z: Float = 0.0f) = Vector3F(x, y, z)

    fun toVector4F(z: Float = 0.0f, w: Float = 0.0f) = Vector4F(x, y, z, w)
}

fun List<Vector2F>.flatten() = FloatArray(this.size * 2).also {
    this.fastForEachIndexed { index, vector ->
        it[index * 2 + 0] = vector.x
        it[index * 2 + 1] = vector.y
    }
}
