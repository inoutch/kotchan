package io.github.inoutch.kotchan.utility.type

data class Mesh(
        private var positions: List<Vector3> = emptyList(),
        private var texcoords: List<Vector2> = emptyList(),
        private var colors: List<Vector4> = emptyList(),
        private var normals: List<Vector3> = emptyList()) {
    val size: Int

    init {
        size = positions.size
        if (size != texcoords.size || size != colors.size || (size != normals.size && normals.isNotEmpty())) {
            throw Error("invalid vertex numbers")
        }
    }

    fun pos() = positions
    fun tex() = texcoords
    fun col() = colors
    fun nom() = normals

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

    fun updateNormals(normals: List<Vector3>) {
        if (this.normals.size == normals.size) {
            this.normals = normals
        }
    }

    fun getVertex(index: Int): Vertex? {
        val p = positions.getOrNull(index)
        val c = colors.getOrNull(index)
        val t = texcoords.getOrNull(index)

        if (p != null && c != null && t != null) {
            return Vertex(p, c, t)
        }
        return null
    }
}
