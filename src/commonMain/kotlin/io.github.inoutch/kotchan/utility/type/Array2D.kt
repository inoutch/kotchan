package io.github.inoutch.kotchan.utility.type

open class Array2D<T>(val size: Point, init: (p: Point) -> T) {
    data class Item<T>(val p: Point, val value: T)

    private val data = MutableList(size.y) { y -> MutableList(size.x) { x -> init(Point(x, y)) } }

    fun get(p: Point) = data.getOrNull(p.y)?.getOrNull(p.x)

    fun set(p: Point, value: T) = data.getOrNull(p.y)?.set(p.x, value)

    fun getAll() = data.mapIndexed { y, row -> row.mapIndexed { x, col -> Item(Point(x, y), col) } }.flatten()
}
