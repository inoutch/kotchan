package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
data class Vector4(var x: Float, val y: Float, val z: Float, val w: Float) {
    companion object {
        val Zero = Vector4()
    }

    constructor() : this(0.0f, 0.0f, 0.0f, 0.0f)
    constructor(v: Float) : this(v, v, v, v)
    constructor(vector3: Vector3, w: Float) : this(vector3.x, vector3.y, vector3.z, w)

    fun copyCoordinatesTo(arr: MutableList<Float>) {
        arr.add(x)
        arr.add(y)
        arr.add(z)
        arr.add(w)
    }

    operator fun times(other: Float) = Vector4(x * other, y * other, z * other, w * other)
    operator fun times(other: Vector4) = Vector4(x * other.x, y * other.y, z * other.z, w * other.w)

    fun flatten() = FloatArray(4).also {
        it[0] = x
        it[1] = y
        it[2] = z
        it[3] = w
    }

    fun toVector2() = Vector2(x, y)
    fun toVector3() = Vector3(x, y, z)
}

fun List<Vector4>.flatten() = FloatArray(this.size * 4).also {
    this.forEachIndexed { index, vector ->
        it[index * 4 + 0] = vector.x
        it[index * 4 + 1] = vector.y
        it[index * 4 + 2] = vector.z
        it[index * 4 + 3] = vector.w
    }
}
