package io.github.inoutch.kotchan.math

import io.github.inoutch.kotchan.extension.fastForEachIndexed
import kotlin.math.sqrt
import kotlinx.serialization.Serializable

@Serializable
data class Vector4F(var x: Float, var y: Float, var z: Float, var w: Float) {
    companion object {
        val Zero: Vector4F
            get() = Vector4F()

        fun covertHsvToRgb(h: Float, s: Float, v: Float): Vector3F {
            var r = v
            var g = v
            var b = v
            val hh = h * 6.0f

            if (s > 0.0f) {
                val i = hh.toInt()
                val f = hh - i
                when (i) {
                    0 -> {
                        g *= 1 - s * (1 - f)
                        b *= 1 - s
                    }
                    1 -> {
                        r *= 1 - s * f
                        b *= 1 - s
                    }
                    2 -> {
                        r *= 1 - s
                        b *= 1 - s * (1 - f)
                    }
                    3 -> {
                        r *= 1 - s
                        g *= 1 - s * f
                    }
                    4 -> {
                        r *= 1 - s * (1 - f)
                        g *= 1 - s
                    }
                    5 -> {
                        g *= 1 - s
                        b *= 1 - s * f
                    }
                }
                return Vector3F(r, g, b)
            }
            return Vector3F(v, v, v)
        }
    }

    private constructor() : this(0.0f, 0.0f, 0.0f, 0.0f)

    constructor(v: Float) : this(v, v, v, v)

    constructor(vector3: Vector3F, w: Float) : this(vector3.x, vector3.y, vector3.z, w)

    fun length() = sqrt(x * x + y * y + z * z + w * w)

    fun normalized(): Vector4F {
        val len = length()
        return Vector4F(x / len, y / len, z / len, w / len)
    }

    fun copyCoordinatesTo(arr: MutableList<Float>) {
        arr.add(x)
        arr.add(y)
        arr.add(z)
        arr.add(w)
    }

    operator fun plus(other: Vector4F) = Vector4F(x + other.x, y + other.y, z + other.z, w + other.w)

    operator fun plus(other: Float) = Vector4F(x + other, y + other, z + other, w + other)

    operator fun minus(other: Vector4F) = Vector4F(x - other.x, y - other.y, z - other.z, w - other.w)

    operator fun minus(other: Float) = Vector4F(x - other, y - other, z - other, w - other)

    operator fun times(other: Vector4F) = Vector4F(x * other.x, y * other.y, z * other.z, w * other.w)

    operator fun times(other: Float) = Vector4F(x * other, y * other, z * other, w * other)

    operator fun div(other: Float) = Vector4F(x / other, y / other, z / other, w / other)

    operator fun div(other: Vector4F) = Vector4F(x / other.x, y / other.y, z / other.z, w / other.w)

    fun flatten() = FloatArray(4).also {
        it[0] = x
        it[1] = y
        it[2] = z
        it[3] = w
    }

    fun toVector2F() = Vector2F(x, y)

    fun toVector3F() = Vector3F(x, y, z)
}

fun List<Vector4F>.flatten() = FloatArray(this.size * 4).also {
    this.fastForEachIndexed { index, vector ->
        it[index * 4 + 0] = vector.x
        it[index * 4 + 1] = vector.y
        it[index * 4 + 2] = vector.z
        it[index * 4 + 3] = vector.w
    }
}
