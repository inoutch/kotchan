package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
data class Rect(val origin: Vector2 = Vector2(), val size: Vector2 = Vector2()) {

    operator fun plus(other: Vector2) = Rect(origin + other, size)

    operator fun div(other: Float) = Rect(this.origin / other, this.size / other)
}
