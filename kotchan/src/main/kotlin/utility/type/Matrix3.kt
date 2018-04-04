package utility.type

data class Matrix3(val col1: Vector3, val col2: Vector3, val col3: Vector3) {
    companion object {
        fun createDiagonal(d1: Float, d2: Float, d3: Float) = Matrix3(
                col1 = Vector3(d1, 0.0f, 0.0f),
                col2 = Vector3(0.0f, d2, 0.0f),
                col3 = Vector3(0.0f, 0.0f, d3)
        )
    }

    constructor() : this(
            Vector3(1.0f, 0.0f, 0.0f),
            Vector3(0.0f, 1.0f, 0.0f),
            Vector3(0.0f, 0.0f, 1.0f))

    operator fun times(other: Vector3) = Vector3(
            col1.x * other.x + col2.x * other.y + col3.x * other.z,
            col1.y * other.x + col2.y * other.y + col3.y * other.z,
            col1.z * other.x + col2.z * other.y + col3.z * other.z
    )

    operator fun times(other: Matrix3) = Matrix3(
            col1 = this * other.col1,
            col2 = this * other.col2,
            col3 = this * other.col3
    )
}