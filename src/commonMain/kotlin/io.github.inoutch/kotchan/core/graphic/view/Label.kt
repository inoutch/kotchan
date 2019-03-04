//package io.github.inoutch.kotchan.core.graphic.view
//
//import io.github.inoutch.kotchan.core.KotchanCore
//import io.github.inoutch.kotchan.utility.font.BMFont
//import io.github.inoutch.kotchan.utility.graphic.GLTexture
//import io.github.inoutch.kotchan.utility.path.Path
//import io.github.inoutch.kotchan.utility.type.Mesh
//import io.github.inoutch.kotchan.utility.type.Vector2
//import io.github.inoutch.kotchan.utility.type.Vector3
//import io.github.inoutch.kotchan.utility.type.Vector4
//
//class Label(private val bmFont: BMFont, textureDir: String, initText: String) : ViewBase(), View2DBase {
//    override var size = Vector2()
//        private set
//
//    override var anchorPoint = Vector2(0.5f, 0.5f)
//        set(value) {
//            field = value
//            isPositionsDirty = true
//        }
//
//    var text = initText
//        set(value) {
//            field = value
//
//            val label = createLabel(value)
//            size = label.size
//
//            labelViews.forEachIndexed { index, view ->
//                view.replaceMesh(label.meshes[index]?.toMesh() ?: Mesh())
//            }
//        }
//
//    val textures = bmFont.pages
//            .map { it.id to KotchanCore.instance.gl.loadTexture(Path.resolve(textureDir, it.file)) }
//            .toMap()
//
//    private val labelViews: List<View>
//
//    private data class LabelMesh(
//            val page: Int,
//            val positions: MutableList<Vector3> = mutableListOf(),
//            val texcoords: MutableList<Vector2> = mutableListOf(),
//            val colors: MutableList<Vector4> = mutableListOf()) {
//        fun toMesh() = Mesh(positions, texcoords, colors)
//    }
//
//    private data class LabelBundle(val meshes: Map<Int, LabelMesh>, val size: Vector2)
//
//    init {
//        val label = this.createLabel(this.text)
//        size = label.size
//
//        labelViews = label.meshes.map { View(it.value.toMesh(), textures[it.value.page] ?: GLTexture.empty) }
//        addChildren(labelViews)
//    }
//
//    private fun createLabel(text: String): LabelBundle {
//        val lines = text.count { it == '\n' } + 1
//
//        val meshes = mutableMapOf<Int, LabelMesh>()
//        var x = 0.0f
//        var y = lines * bmFont.common.lineHeight.toFloat()
//
//        for (i in 0 until text.length) {
//            if (text[i] == '\n') {
//                x = 0.0f
//                y -= bmFont.common.lineHeight
//                continue
//            }
//
//            val char = bmFont.chars[text[i].toInt()] ?: continue
//            val next = text.getOrNull(i + 1)?.let { bmFont.chars[it.toInt()] }
//
//            val amount = next?.let { bmFont.kernings[char.id]?.get(it.id) } ?: 0
//            val offset = Vector2(
//                    char.offset.x + amount + x,
//                    y - char.offset.y.toFloat() - char.rect.size.y)
//
//            x += char.xAdvance
//
//            val texture = textures[char.page] ?: continue
//            val mesh = meshes.getOrPut(char.page) { LabelMesh(char.page) }
//            val textureSize = Vector2(texture.width, texture.height)
//
//            mesh.positions.addAll(Square.createSquarePositions(offset, char.rect.size.toVector2()))
//            mesh.texcoords.addAll(Square.createSquareTexcoords(
//                    char.rect.origin / textureSize, char.rect.size / textureSize))
//            mesh.colors.addAll(List(6) { color })
//        }
//        return LabelBundle(meshes, Vector2(x, y))
//    }
//}
