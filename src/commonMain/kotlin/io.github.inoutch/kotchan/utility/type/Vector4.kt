package io.github.inoutch.kotchan.utility.type

data class Vector4(var x: Float, val y: Float, val z: Float, val w: Float) {
    companion object {
        val Zero = Vector4()
    }

    constructor() : this(0.0f, 0.0f, 0.0f, 0.0f)
    constructor(vector3: Vector3, w: Float) : this(vector3.x, vector3.y, vector3.z, w)

    fun copyCoordinatesTo(arr: MutableList<Float>) {
        arr.add(x)
        arr.add(y)
        arr.add(z)
        arr.add(w)
    }

    operator fun times(other: Float) = Vector4(x * other, y * other, z * other, w * other)
}

fun List<Vector4>.flatten() = FloatArray(this.size * 4).also {
    this.forEachIndexed { index, vector ->
        it[index * 4 + 0] = vector.x
        it[index * 4 + 1] = vector.y
        it[index * 4 + 2] = vector.z
        it[index * 4 + 3] = vector.w
    }
}