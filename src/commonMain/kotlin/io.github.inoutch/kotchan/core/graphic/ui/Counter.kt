package io.github.inoutch.kotchan.core.graphic.ui

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon2D
import io.github.inoutch.kotchan.core.graphic.template.max
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.utility.type.*
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class Counter(
    material: Material,
    private val textureAtlas: TextureAtlas,
    initValue: Int,
    private val numberNames: List<String>
) :
        Polygon2D(Mesh(), material, Vector2.Zero) {

    var value: Int = 0
        set(value) {
            val v = if (value < 0) 0 else value
            replaceNumbers(v)
            field = v
        }

    init {
        value = initValue
    }

    private fun digit(value: Int) = if (value == 0) 1 else floor(log10(value.toFloat())).toInt() + 1

    private fun replaceNumbers(value: Int) {
        val d = digit(value)
        val n10 = 10.0f
        var width = 0.0f
        val meshes = mutableListOf<Mesh>()

        val frameMeshes = List(d) {
            val n = (value / n10.pow(it)).toInt() % 10
            textureAtlas.frameMesh(numberNames[n])
                    ?: throw Error("${numberNames[n]} not contains in texture atlas")
        }.reversed()

        val heightMax = frameMeshes.max { it.first[1].y - it.first[0].y }

        for (frameMesh in frameMeshes) {
            val dx = frameMesh.first[1].x - frameMesh.first[0].x
            val dy = frameMesh.first[1].y - frameMesh.first[0].y
            val pos = frameMesh.first.map {
                Vector3(it.x + width, it.y + (heightMax - dy) / 2.0f, it.z)
            }
            val tex = frameMesh.second
            meshes.add(Mesh(pos, tex, List(frameMesh.first.size) { Vector4(1.0f) }))
            width += dx
        }

        size = Vector2(width, heightMax)
        replaceMesh(meshes.combine())
    }
}
