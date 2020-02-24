package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.BufferInterface
import io.github.inoutch.kotchan.core.graphic.ChangeRange
import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.core.tool.BMFontLoader
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

open class LabelSprite private constructor(
    initialText: String,
    private val bmFont: BMFontLoader.BMFont,
    private var labelMesh: LabelMesh
) : Polygon(labelMesh.toMesh()) {
    companion object {
        fun create(
            initialText: String,
            bmFont: BMFontLoader.BMFont,
            fontSize: Float
        ): LabelSprite {
            return LabelSprite(initialText, bmFont, createLabel(bmFont, initialText, Vector4F(1.0f), fontSize))
        }

        private fun createLabel(
            bmFont: BMFontLoader.BMFont,
            text: String,
            color: Vector4F,
            fontSize: Float = 1.0f
        ): LabelMesh {
            val lineSize = text.count { it == '\n' } + 1
            val fontRatio = if (fontSize <= 0.0f) 1.0f else fontSize / bmFont.common.lineHeight
            val lineHeight = fontRatio * bmFont.common.lineHeight

            var x = 0.0f
            var y = lineSize * lineHeight
            val mesh = LabelMesh()

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
                mesh.positions.addAll(Sprite.createSquarePositions(offset, charRect.size))
                mesh.texcoords.addAll(Sprite.createSquareTexcoords(
                        char.rect.origin / bmFont.common.scale.toVector2F(),
                        char.rect.size / bmFont.common.scale.toVector2F()
                ))
                mesh.colors.addAll(List(6) { color })
                mesh.texNumbers.addAll(List(6) { texNumber })
            }
            return mesh
        }
    }

    private data class LabelMesh(
        val positions: MutableList<Vector3F> = mutableListOf(),
        val texcoords: MutableList<Vector2F> = mutableListOf(),
        val colors: MutableList<Vector4F> = mutableListOf(),
        val texNumbers: MutableList<Int> = mutableListOf()
    ) {
        fun toMesh() = Mesh(positions, texcoords, colors)
    }

    var text: String = initialText
        set(value) {
            field = value
            labelMesh = createLabel(bmFont, text, color)
            updateMesh(labelMesh.toMesh())
            texNumbersChange.reset()
        }

    protected val texNumbersChange = ChangeRange(mesh.size)

    fun copyTexNumbersTo(target: BufferInterface<Float>, offset: Int = 0) {
        val change = texNumbersChange.change ?: return
        var i = change.first
        while (i < change.last) {
            val t = labelMesh.texNumbers[i]
            target.copy(offset + i, t.toFloat())
            i++
        }
        target.range(offset + change.first, offset + change.last)
        texNumbersChange.reset()
    }
}
