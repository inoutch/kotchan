package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
data class Rect(val origin: Vector2, val size: Vector2) {
    companion object {
        val Zero = Rect(Vector2.Zero, Vector2.Zero)
    }

    operator fun plus(other: Vector2) = Rect(origin + other, size)

    operator fun minus(other: Vector2) = Rect(origin - other, size)

    operator fun times(other: Vector2) = Rect(origin * other, size * other)

    operator fun times(other: Float) = Rect(origin * other, size * other)

    operator fun div(other: Float) = Rect(this.origin / other, this.size / other)
}
