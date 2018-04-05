package utility.type

data class Matrix4(val col1: Vector4, val col2: Vector4, val col3: Vector4, val col4: Vector4) {
    companion object {
        fun createTranslation(vector: Vector3) = Matrix4(
                Matrix3(),
                Vector4(vector.x, vector.y, vector.z, 1.0f))
    }

    constructor() : this(
            Vector4(1.0f, 0.0f, 0.0f, 0.0f),
            Vector4(0.0f, 1.0f, 0.0f, 0.0f),
            Vector4(0.0f, 0.0f, 1.0f, 0.0f),
            Vector4(0.0f, 0.0f, 0.0f, 1.0f))

    constructor(matrix3: Matrix3, col4: Vector4 = Vector4(0.0f, 0.0f, 0.0f, 1.0f)) : this(
            col1 = Vector4(matrix3.col1, 0.0f),
            col2 = Vector4(matrix3.col2, 0.0f),
            col3 = Vector4(matrix3.col3, 0.0f),
            col4 = col4
    )

    fun flatten(): FloatArray {
        val values = mutableListOf<Float>()
        col1.copyCoordinatesTo(values)
        col2.copyCoordinatesTo(values)
        col3.copyCoordinatesTo(values)
        col4.copyCoordinatesTo(values)
        return values.toFloatArray()
    }

    operator fun times(other: Vector4) = Vector4(
            col1.x * other.x + col2.x * other.y + col3.x * other.z + col4.x * other.w,
            col1.y * other.x + col2.y * other.y + col3.y * other.z + col4.y * other.w,
            col1.z * other.x + col2.z * other.y + col3.z * other.z + col4.z * other.w,
            col1.w * other.x + col2.w * other.y + col3.w * other.z + col4.w * other.w
    )

    operator fun times(other: Matrix4) = Matrix4(
            col1 = this * other.col1,
            col2 = this * other.col2,
            col3 = this * other.col3,
            col4 = this * other.col4
    )
}