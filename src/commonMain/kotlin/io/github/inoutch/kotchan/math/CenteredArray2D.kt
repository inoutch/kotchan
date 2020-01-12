package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
data class CenteredArray2D<T>(val size: Vector2I, val center: Vector2I, val values: MutableList<T>) {
    companion object {
        fun <T> create(size: Vector2I, center: Vector2I, values: List<T>): CenteredArray2D<T> {
            return CenteredArray2D(size, center, values.toMutableList())
        }
    }

    operator fun get(p: Vector2I): T? {
        val actual = p + center
        return if (size.inRange(actual)) values[actual.y * size.x + actual.x] else null
    }

    operator fun set(p: Vector2I, value: T) {
        val actual = p + center
        if (size.inRange(actual)) {
            values[actual.y * size.x + actual.x] = value
        }
    }

    fun getValue(p: Vector2I): T {
        val actual = p + center
        return values[actual.y * size.x + actual.x]
    }

    fun getAll(): List<Pair<Vector2I, T>> = values.mapIndexed { index, t ->
        Pair(Vector2I(index % size.x - center.x, index / size.y - center.y), t)
    }
}
