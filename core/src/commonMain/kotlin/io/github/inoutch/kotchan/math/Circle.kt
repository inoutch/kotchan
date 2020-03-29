package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
data class Circle(val center: Vector2F, val radius: Float) {
    companion object {
        fun collision(c1: Circle, c2: Circle): Boolean {
            val d = c1.center - c2.center
            val r = c1.radius + c2.radius
            return d.x * d.x + d.y * d.y < r * r
        }
    }

    fun collision(other: Circle): Boolean {
        val d = center - other.center
        val r = radius + other.radius
        return d.x * d.x + d.y * d.y < r * r
    }
}
