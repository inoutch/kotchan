package io.github.inoutch.kotchan.core.math

import kotlin.math.sqrt
import kotlinx.serialization.Serializable

@Serializable
data class Vector2I(var x: Int, var y: Int) {
    companion object {
        val Zero: Vector2I
            get() = Vector2I(0, 0)
    }

    private constructor() : this(0, 0)

    constructor(vector: Vector2F) : this(vector.x.toInt(), vector.y.toInt())

    constructor(x: Float, y: Float) : this(x.toInt(), y.toInt())

    fun length() = sqrt((x * x + y * y).toFloat())

    fun inRange(p: Vector2I) = 0 <= p.x && 0 <= p.y && p.x < x && p.y < y

    operator fun plus(other: Vector2I) = Vector2I(x + other.x, y + other.y)

    operator fun plus(other: Vector2F) = Vector2F(x + other.x, y + other.y)

    operator fun minus(other: Vector2I) = Vector2I(x - other.x, y - other.y)

    operator fun minus(other: Vector2F) = Vector2F(x - other.x, y - other.y)

    operator fun times(other: Vector2I) = Vector2I(x * other.x, y * other.y)

    operator fun times(other: Vector2F) = Vector2F(x * other.x, y * other.y)

    operator fun times(other: Int) = Vector2I(x * other, y * other)

    operator fun div(other: Vector2I) = Vector2I(x / other.x, y / other.y)

    operator fun div(other: Vector2F) = Vector2F(x / other.x, y / other.y)

    operator fun div(other: Float) = Vector2F(x / other, y / other)

    operator fun div(other: Int) = Vector2I(x / other, y / other)

    fun toVector3I(z: Int = 0) = Vector3I(x, y, z)

    fun toVector4I(z: Int = 0, w: Int = 0) = Vector4I(x, y, z, w)

    fun toVector2F() = Vector2F(x.toFloat(), y.toFloat())

    fun toVector3F(z: Float = 0.0f) = Vector3F(x.toFloat(), y.toFloat(), z)

    fun toVector4F(z: Float = 0.0f, w: Float = 0.0f) = Vector4F(x.toFloat(), y.toFloat(), z, w)
}

fun Vector2F.toVector2I() = Vector2I(this.x.toInt(), this.y.toInt())
