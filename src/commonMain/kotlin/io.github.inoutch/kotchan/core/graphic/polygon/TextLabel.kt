package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError
import io.github.inoutch.kotchan.utility.path.Path
import io.github.inoutch.kotchan.utility.type.Mesh
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class TextLabel(
        private val bmFont: BMFont,
        materialConfig: Material.Config,
        textureDir: String,
        initText: String)
    : Polygon2D(Mesh(), null, Vector2.Zero) {

    companion object {
        fun loadFromResource(
                filepath: String,
                textureDir: String,
                materialConfig: Material.Config,
                text: String): TextLabel {
            val bmFont = BMFont.loadFromResource(filepath)
            return TextLabel(bmFont, materialConfig, instance.file.getResourcePathWithError(textureDir), text)
        }
    }

    private val polygons: Map<Int, Polygon>

    val materials: Map<Int, Material>

    var text = initText
        set(value) {
            field = value

            val label = createLabel(value)
            size = label.size
            label.meshes.forEach { polygons[it.key]?.replaceMesh(it.value.toMesh()) }
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
        materials = bmFont.pages.map {
            val texture = Texture.load(Path.resolve(textureDir, it.file))
            it.id to Material(materialConfig, listOf(texture ?: Texture.emptyTexture()))
        }.toMap()

        val label = this.createLabel(this.text)
        size = label.size

        polygons = label.meshes.map { it.key to Polygon(it.value.toMesh(), it.value.material) }.toMap()
        addChildren(polygons.values.toList())
    }

    private fun createLabel(text: String): LabelBundle {
        val lines = text.count { it == '\n' } + 1

        val meshes = mutableMapOf<Int, LabelMesh>()
        var x = 0.0f
        var y = lines * bmFont.common.lineHeight.toFloat()

        for (i in 0 until text.length) {
            if (text[i] == '\n') {
                x = 0.0f
                y -= bmFont.common.lineHeight
                continue
            }

            val char = bmFont.chars[text[i].toInt()] ?: continue
            val next = text.getOrNull(i + 1)?.let { bmFont.chars[it.toInt()] }

            val amount = next?.let { bmFont.kernings[char.id]?.get(it.id) } ?: 0
            val offset = Vector2(
                    char.offset.x + amount + x,
                    y - char.offset.y.toFloat() - char.rect.size.y)

            x += char.xAdvance

            val material = materials[char.page] ?: continue
            val texture = material.textures.firstOrNull() ?: continue
            val mesh = meshes.getOrPut(char.page) { LabelMesh(char.page, material) }
            mesh.positions.addAll(Sprite.createSquarePositions(offset, char.rect.size.toVector2()))
            mesh.texcoords.addAll(Sprite.createSquareTexcoords(
                    char.rect.origin / texture.size.toVector2(),
                    char.rect.size / texture.size.toVector2()))
            mesh.colors.addAll(List(6) { color })
        }
        return LabelBundle(meshes, Vector2(x, y))
    }
}
