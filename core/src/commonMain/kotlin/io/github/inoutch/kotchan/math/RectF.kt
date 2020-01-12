package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
data class RectF(val origin: Vector2F, val size: Vector2F) {
    companion object {
        val Zero = RectF(Vector2F.Zero, Vector2F.Zero)
    }

    constructor(origin: Vector2I, size: Vector2I) : this(origin.toVector2F(), size.toVector2F())

    operator fun plus(other: Vector2F) = RectF(origin + other, size)

    operator fun minus(other: Vector2F) = RectF(origin - other, size)

    operator fun times(other: Vector2F) = RectF(origin * other, size * other)

    operator fun times(other: Float) = RectF(origin * other, size * other)

    operator fun div(other: Float) = RectF(this.origin / other, this.size / other)
}
