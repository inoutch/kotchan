package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

class Mesh(
        initPositions: List<Vector3F> = emptyList(),
        initTexcoords: List<Vector2F> = emptyList(),
        initColors: List<Vector4F> = emptyList(),
        initNormals: List<Vector3F> = emptyList()
) {

    private var positions: MutableList<Vector3F> = initPositions.toMutableList()

    private var texcoords: MutableList<Vector2F> = initTexcoords.toMutableList()

    private var colors: MutableList<Vector4F> = initColors.toMutableList()

    private var normals: MutableList<Vector3F> = initNormals.toMutableList()

    val size: Int = positions.size

    init {
        if (size != texcoords.size || size != colors.size || (size != normals.size && normals.isNotEmpty())) {
            throw IllegalArgumentException("Invalid vertex size")
        }
    }

    fun pos() = positions

    fun tex() = texcoords

    fun col() = colors

    fun nom() = normals

    fun updatePositions(positions: List<Vector3F>, offset: Int = 0) {
        if (this.positions.size >= positions.size + offset) {
            positions.forEachIndexed { i, x -> this.positions[i + offset] = x }
        }
    }

    fun updateTexcoords(texcoords: List<Vector2F>, offset: Int = 0) {
        if (this.texcoords.size >= texcoords.size + offset) {
            texcoords.forEachIndexed { i, x -> this.texcoords[i + offset] = x }
        }
    }

    fun updateColors(colors: List<Vector4F>, offset: Int = 0) {
        if (this.colors.size >= colors.size + offset) {
            colors.forEachIndexed { i, x -> this.colors[i + offset] = x }
        }
    }

    fun updateNormals(normals: List<Vector3F>, offset: Int = 0) {
        if (this.normals.size >= normals.size + offset) {
            normals.forEachIndexed { i, x -> this.normals[i + offset] = x }
        }
    }

    operator fun get(index: Int): Vertex? {
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
