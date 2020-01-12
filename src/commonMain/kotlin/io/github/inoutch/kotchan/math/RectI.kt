package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
data class RectI(val origin: Vector2I, val size: Vector2I) {
    constructor(x: Int, y: Int, w: Int, h: Int) : this(Vector2I(x, y), Vector2I(w, h))

    fun toRectF() = RectF(this.origin.toVector2F(), this.size.toVector2F())

    operator fun plus(other: Vector2I) = RectI(origin + other, size)

    operator fun minus(other: Vector2I) = RectI(origin - other, size)

    operator fun times(other: Vector2I) = RectI(origin * other, size * other)

    operator fun times(other: Int) = RectI(origin * other, size * other)

    operator fun div(other: Int) = RectI(this.origin / other, this.size / other)

    operator fun div(other: Float) = RectF(this.origin / other, this.size / other)
}
