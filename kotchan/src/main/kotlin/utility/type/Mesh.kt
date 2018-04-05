package utility.type

data class Mesh(
        private var positions: List<Vector3>,
        private var texcoords: List<Vector2>,
        private var colors: List<Vector4>) {
    fun pos() = positions
    fun tex() = texcoords
    fun col() = colors

    fun updatePositions(positions: List<Vector3>) {
        if (this.positions.size == positions.size) {
            this.positions = positions
        }
    }
    fun updateTexcoords(texcoords: List<Vector2>) {
        if (this.texcoords.size == texcoords.size) {
            this.texcoords = texcoords
        }
    }
    fun updateColors(colors: List<Vector4>) {
        if (this.colors.size == colors.size) {
            this.colors = colors
        }
    }
}