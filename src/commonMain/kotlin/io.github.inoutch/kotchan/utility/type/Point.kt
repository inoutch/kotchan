package io.github.inoutch.kotchan.utility.type

data class Point(val x: Int, val y: Int) {
    constructor() : this(0, 0)
    constructor(vector: Vector2) : this(vector.x.toInt(), vector.y.toInt())
    constructor(x: Float, y: Float) : this(x.toInt(), y.toInt())

    fun toVector2() = Vector2(x.toFloat(), y.toFloat())
    fun toVector3(z: Float = 0.0f) = Vector3(x.toFloat(), y.toFloat(), z)
    fun toVector4(z: Float = 0.0f, w: Float = 0.0f) = Vector4(x.toFloat(), y.toFloat(), z, w)

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
    operator fun times(other: Point) = Point(x * other.x, y * other.y)
    operator fun times(other: Vector2) = Vector2(x * other.x, y * other.y)
    operator fun div(other: Point) = Point(x / other.x, y / other.y)
    operator fun div(other: Vector2) = Vector2(x / other.x, y / other.y)
    operator fun div(other: Float) = Vector2(x / other, y / other)
    operator fun div(other: Int) = Point(x / other, y / other)
    fun equals(other: Point) = x == other.x && y == other.y
}

fun Vector2.toPoint() = Point(this.x.toInt(), this.y.toInt())