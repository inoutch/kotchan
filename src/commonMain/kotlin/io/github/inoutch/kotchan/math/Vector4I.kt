package io.github.inoutch.kotchan.math

import kotlin.math.sqrt
import kotlinx.serialization.Serializable

@Serializable
data class Vector4I(var x: Int, var y: Int, var z: Int, var w: Int) {
    companion object {
        val Zero: Vector4I
            get() = Vector4I()
    }

    private constructor() : this(0, 0, 0, 0)

    constructor(value: Int) : this(value, value, value, value)

    constructor(vector: Vector4F) : this(vector.x.toInt(), vector.y.toInt(), vector.z.toInt(), vector.w.toInt())

    fun length() = sqrt((x * x + y * y + z * z + w * w).toFloat())

    operator fun plus(other: Vector4I) = Vector4I(x + other.x, y + other.y, z + other.z, w + other.w)

    operator fun plus(other: Int) = Vector4I(x + other, y + other, z + other, w + other)

    operator fun minus(other: Vector4I) = Vector4I(x - other.x, y - other.y, z - other.z, w - other.w)

    operator fun minus(other: Int) = Vector4I(x - other, y - other, z - other, w - other)

    operator fun times(other: Int) = Vector4I(x * other, y * other, z * other, w * other)

    operator fun times(other: Vector4I) = Vector4I(x * other.x, y * other.y, z * other.z, w * other.w)

    operator fun div(other: Int) = Vector4I(x / other, y / other, z / other, w / other)

    operator fun div(other: Vector4I) = Vector4I(x / other.x, y / other.y, z / other.z, w / other.w)
}
