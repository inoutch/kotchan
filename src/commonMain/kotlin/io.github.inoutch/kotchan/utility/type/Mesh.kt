package io.github.inoutch.kotchan.utility.type

class Mesh(
    initPositions: List<Vector3> = emptyList(),
    initTexcoords: List<Vector2> = emptyList(),
    initColors: List<Vector4> = emptyList(),
    initNormals: List<Vector3> = emptyList()
) {

    private var positions: MutableList<Vector3> = initPositions.toMutableList()
    private var texcoords: MutableList<Vector2> = initTexcoords.toMutableList()
    private var colors: MutableList<Vector4> = initColors.toMutableList()
    private var normals: MutableList<Vector3> = initNormals.toMutableList()

    val size: Int = positions.size

    init {
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
            this.positions = positions.toMutableList()
        }
    }

    fun updateTexcoords(texcoords: List<Vector2>) {
        if (this.texcoords.size == texcoords.size) {
            this.texcoords = texcoords.toMutableList()
        }
    }

    fun updateColors(colors: List<Vector4>) {
        if (this.colors.size == colors.size) {
            this.colors = colors.toMutableList()
        }
    }

    fun updateNormals(normals: List<Vector3>) {
        if (this.normals.size == normals.size) {
            this.normals = normals.toMutableList()
        }
    }

    fun updatePositions(positions: List<Vector3>, offset: Int) {
        if (this.positions.size >= positions.size + offset) {
            positions.forEachIndexed { i, x -> this.positions[i + offset] = x }
        }
    }

    fun updateTexcoords(texcoords: List<Vector2>, offset: Int) {
        if (this.texcoords.size >= texcoords.size + offset) {
            texcoords.forEachIndexed { i, x -> this.texcoords[i + offset] = x }
        }
    }

    fun updateColors(colors: List<Vector4>, offset: Int) {
        if (this.colors.size >= colors.size + offset) {
            colors.forEachIndexed { i, x -> this.colors[i + offset] = x }
        }
    }

    fun updateNormals(normals: List<Vector3>, offset: Int) {
        if (this.normals.size >= normals.size + offset) {
            normals.forEachIndexed { i, x -> this.normals[i + offset] = x }
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

fun List<Mesh>.combine(): Mesh {
    val pos = map { it.pos() }.flatten()
    val tex = map { it.tex() }.flatten()
    val col = map { it.col() }.flatten()
    val nom = map { it.nom() }.flatten()
    return Mesh(pos, tex, col, nom)
}
