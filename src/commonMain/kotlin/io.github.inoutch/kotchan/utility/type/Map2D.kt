package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
class Map2D<T> {
    private val data = mutableMapOf<Int, MutableMap<Int, T>>()

    operator fun get(p: Point): T? {
        return data.getOrPut(p.y) { mutableMapOf() }[p.x]
    }

    operator fun set(p: Point, value: T) {
        data.getOrPut(p.y) { mutableMapOf() }[p.x] = value
    }

    fun getOrPut(p: Point, put: () -> T): T {
        this[p]?.let { return it }
        return put().also { this[p] = it }
    }

    fun getAll(): List<Pair<Point, T>> {
        return data.map { y ->
            y.value.map { x ->
                Pair(Point(x.key, y.key), x.value)
            }
        }.flatten()
    }
}
