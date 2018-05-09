package utility.type

import kotlin.math.sqrt

data class Vector3(val x: Float, val y: Float, val z: Float) {
    constructor() : this(0.0f, 0.0f, 0.0f)
    constructor(vector2: Vector2, z: Float) : this(vector2.x, vector2.y, z)
    constructor(vector4: Vector4) : this(vector4.x, vector4.y, vector4.z)

    val length get() = sqrt(x * x + y * y + z * z)

    fun crossProduct(other: Vector3): Vector3 =
            Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

    fun dotProduct(other: Vector3) = (x * other.x + y * other.y + z * other.z)

    fun normalized(): Vector3 {
        val len = length
        return Vector3(x / len, y / len, z / len)
    }

    fun copyCoordinatesTo(arr: MutableList<Float>) {
        arr.add(x)
        arr.add(y)
        arr.add(z)
    }

    operator fun minus(other: Vector3) = Vector3(x - other.x, y - other.y, z - other.z)
    operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
    operator fun times(other: Float) = Vector3(x * other, y * other, z * other)
    operator fun times(other: Vector3) = Vector3(x * other.x, y * other.y, z * other.z)

    companion object {
        val Zero = Vector3(0.0f, 0.0f, 0.0f)
    }
}

fun List<Vector3>.flatten() = FloatArray(this.size * 3).also {
    this.forEachIndexed { index, vector ->
        it[index * 3 + 0] = vector.x
        it[index * 3 + 1] = vector.y
        it[index * 3 + 2] = vector.z
    }
}