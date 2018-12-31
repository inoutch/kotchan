package io.github.inoutch.kotchan.utility.type

data class PointRect(val origin: Point = Point(), val size: Point = Point()) {
    constructor(x: Int, y: Int, w: Int, h: Int) : this(Point(x, y), Point(w, h))

    fun toRect() = Rect(this.origin.toVector2(), this.size.toVector2())

    operator fun plus(other: Point) = PointRect(origin + other)
    operator fun div(other: Float) = Rect(this.origin / other, this.size / other)
}