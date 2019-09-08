package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.type.*

open class TextLabel(
        private val bmFont: BMFont,
        initText: String,
        private val fontSize: Float = 0.0f) : Polygon2D(Mesh(), null, Vector2.Zero) {

    private val polygons: Map<Int, Polygon2D> = bmFont.pages
            .map { it.id to Polygon2D(Mesh(), bmFont.materials.getValue(it.id), Vector2.Zero) }
            .toMap()

    var text = initText
        set(value) {
            field = value

            val label = createLabel(value)
            size = label.size
            polygons.values.forEach {
                it.replaceMesh(Mesh())
                it.size = size
            }
            label.meshes.forEach {
                val polygon = polygons.getValue(it.key)
                polygon.replaceMesh(it.value.toMesh())
                polygon.size = size
            }
        }

    private data class LabelMesh(
            val page: Int,
            val material: Material,
            val positions: MutableList<Vector3> = mutableListOf(),
            val texcoords: MutableList<Vector2> = mutableListOf(),
            val colors: MutableList<Vector4> = mutableListOf()) {
        fun toMesh() = Mesh(positions, texcoords, colors)
    }

    private data class LabelBundle(val meshes: Map<Int, LabelMesh>, val size: Vector2)

    init {
        addChildren(polygons.values.toList())
        text = initText
    }

    private fun createLabel(text: String): LabelBundle {
        val lines = text.count { it == '\n' } + 1

        val fontRatio = if (fontSize <= 0.0f) 1.0f else fontSize / bmFont.common.lineHeight
        val lineHeight = fontRatio * bmFont.common.lineHeight
        val meshes = mutableMapOf<Int, LabelMesh>()
        var x = 0.0f
        var y = lines * lineHeight

        for (i in 0 until text.length) {
            if (text[i] == '\n') {
                x = 0.0f
                y -= lineHeight
                continue
            }

            val char = bmFont.chars[text[i].toInt()] ?: continue
            val charOffset = char.offset.toVector2() * fontRatio
            val charRect = char.rect.toRect() * fontRatio
            val next = text.getOrNull(i + 1)?.let { bmFont.chars[it.toInt()] }

            val amount = (next?.let { bmFont.kernings[char.id]?.get(it.id) } ?: 0) * fontRatio
            val offset = Vector2(charOffset.x + amount + x, y - charOffset.y - charRect.size.y)

            x += char.xAdvance * fontRatio

            val materials = bmFont.materials
            val material = materials[char.page] ?: continue
            val texture = material.textures.firstOrNull() ?: continue
            val mesh = meshes.getOrPut(char.page) { LabelMesh(char.page, material) }
            mesh.positions.addAll(Sprite.createSquarePositions(offset, charRect.size))
            mesh.texcoords.addAll(Sprite.createSquareTexcoords(
                    char.rect.origin / texture.size.toVector2(),
                    char.rect.size / texture.size.toVector2()))
            mesh.colors.addAll(List(6) { color })
        }
        return LabelBundle(meshes, Vector2(x, y))
    }
}
