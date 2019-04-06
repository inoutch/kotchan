package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
open class Array2D<T>(val size: Point, private val data: MutableList<MutableList<T>>) {
    companion object {
        fun <T> create(size: Point, init: (p: Point) -> T): Array2D<T> {
            return Array2D(size, MutableList(size.y) { y -> MutableList(size.x) { x -> init(Point(x, y)) } })
        }
    }

    @Serializable
    data class Item<T>(val p: Point, val value: T)

    operator fun get(p: Point) = data.getOrNull(p.y)?.getOrNull(p.x)

    operator fun set(p: Point, value: T) = data.getOrNull(p.y)?.set(p.x, value)

    fun getAll() = data.mapIndexed { y, row -> row.mapIndexed { x, col -> Item(Point(x, y), col) } }.flatten()
}
