package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
data class CenteredArray2D<T>(val size: Point, val center: Point, val values: MutableList<T>) {
    companion object {
        fun <T> create(size: Point, center: Point, values: List<T>): CenteredArray2D<T> {
            return CenteredArray2D(size, center, values.toMutableList())
        }
    }

    operator fun get(p: Point): T? {
        val actual = p + center
        return if (size.inRange(actual)) values[actual.y * size.x + actual.x] else null
    }

    operator fun set(p: Point, value: T) {
        val actual = p + center
        if (size.inRange(actual)) {
            values[actual.y * size.x + actual.x] = value
        }
    }

    fun getAll(): List<Pair<Point, T>> = values.mapIndexed { index, t ->
        Pair(Point(index % size.x - center.x, index / size.y - center.y), t)
    }

    fun getCenterValue() = get(Point.ZERO)
}
