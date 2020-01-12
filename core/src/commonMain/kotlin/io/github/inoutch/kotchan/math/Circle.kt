package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
data class Circle(val center: Vector2F, val radius: Float) {
    fun collision(other: Circle): Boolean {
        val d = center - other.center
        val r = radius + other.radius
        return d.x * d.x + d.y * d.y < r * r
    }
}
