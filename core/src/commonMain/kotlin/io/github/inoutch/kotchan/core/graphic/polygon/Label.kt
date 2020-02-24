package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.core.tool.BMFontLoader
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

/*class Label : Polygon() {

    companion object {
        fun create(bmFont: BMFontLoader.BMFont, text: String, fontSize: Float = 1.0f): Label {
            val lineSize = text.count { it == '\n' } + 1
            val fontRatio = if (fontSize <= 0.0f) 1.0f else fontSize / bmFont.common.lineHeight
            val lineHeight = fontRatio * bmFont.common.lineHeight
            val meshes = mutableMapOf<Int, LabelMesh>()

            var x = 0.0f
            var y = lineSize * lineHeight

            for (i in text.indices) {
                val charRaw = text[i]
                if (charRaw == '\n') {
                    x = 0.0f
                    y -= lineHeight
                    continue
                }

                val char = bmFont.chars[charRaw.toInt()] ?: continue
                val charOffset = char.offset.toVector2F() * fontRatio
                val charRect = char.rect.toRectF() * fontRatio
                val next = text.getOrNull(i + 1)?.let { bmFont.chars[it.toInt()] }

                val amount = (next?.let { bmFont.kernigs.getAmountOrNull(char.id, it.id) } ?: 0) * fontRatio
                val offset = Vector2F(charOffset.x + amount + x, y - charOffset.y - charRect.size.y)

                x += char.xAdvance * fontRatio

                val texNumber = char.page
                val material = materials[char.page] ?: continue
                val texture = material.textures.firstOrNull() ?: continue
                val mesh = meshes.getOrPut(char.page) { LabelMesh(char.page, material) }
                mesh.positions.addAll(Sprite.createSquarePositions(offset, charRect.size))
                mesh.texcoords.addAll(Sprite.createSquareTexcoords(
                        char.rect.origin / texture.size.toVector2(),
                        char.rect.size / texture.size.toVector2()))
                mesh.colors.addAll(List(6) { color })
            }
        }
    }

    private data class LabelMesh(
            val page: Int,
            val positions: MutableList<Vector3F> = mutableListOf(),
            val texcoords: MutableList<Vector2F> = mutableListOf(),
            val colors: MutableList<Vector4F> = mutableListOf(),
            val texNumbers: MutableList<Int> = mutableListOf()
    ) {
        fun toMesh() = Mesh(positions, texcoords, colors)
    }
}*/
