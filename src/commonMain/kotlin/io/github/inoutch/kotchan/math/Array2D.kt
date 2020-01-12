package io.github.inoutch.kotchan.math

import kotlinx.serialization.Serializable

@Serializable
class Array2D<T>(val size: Vector2I, private val data: MutableList<MutableList<T>>) {
    companion object {
        fun <T> create(size: Vector2I, init: (p: Vector2I) -> T): Array2D<T> {
            return Array2D(size, MutableList(size.y) { y -> MutableList(size.x) { x -> init(Vector2I(x, y)) } })
        }
    }

    @Serializable
    data class Item<T>(val p: Vector2I, val value: T)

    operator fun get(p: Vector2I) = data.getOrNull(p.y)?.getOrNull(p.x)

    operator fun set(p: Vector2I, value: T) = data.getOrNull(p.y)?.set(p.x, value)

    fun getValue(p: Vector2I) = data[p.y][p.x]

    fun getAll() = data.mapIndexed { y, row -> row.mapIndexed { x, col -> Item(Vector2I(x, y), col) } }.flatten()
}
