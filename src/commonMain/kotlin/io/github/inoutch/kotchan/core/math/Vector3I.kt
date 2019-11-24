package io.github.inoutch.kotchan.core.math

import kotlin.math.sqrt
import kotlinx.serialization.Serializable

@Serializable
data class Vector3I(var x: Int, var y: Int, var z: Int) {
    companion object {
        val Zero: Vector3I
            get() = Vector3I(0, 0, 0)
    }

    private constructor() : this(0, 0, 0)

    constructor(vector: Vector3F) : this(vector.x.toInt(), vector.y.toInt(), vector.z.toInt())

    constructor(x: Float, y: Float, z: Float) : this(x.toInt(), y.toInt(), z.toInt())

    fun length() = sqrt((x * x + y * y + z * z).toFloat())

    fun normalized(): Vector3F {
        val len = length()
        return Vector3F(x / len, y / len, z / len)
    }

    operator fun plus(other: Int) = Vector3I(x + other, y + other, z + other)

    operator fun plus(other: Vector3I) = Vector3I(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Int) = Vector3I(x - other, y - other, z - other)

    operator fun minus(other: Vector3I) = Vector3I(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Int) = Vector3I(x * other, y * other, z * other)

    operator fun times(other: Vector3I) = Vector3I(x * other.x, y * other.y, z * other.z)

    operator fun div(other: Int) = Vector3I(x / other, y / other, z / other)

    operator fun div(other: Vector3I) = Vector3I(x / other.x, y / other.y, z / other.z)
}

fun List<Vector3I>.flatten() = IntArray(this.size * 3).also {
    this.forEachIndexed { index, vector ->
        it[index * 3 + 0] = vector.x
        it[index * 3 + 1] = vector.y
        it[index * 3 + 2] = vector.z
    }
}
