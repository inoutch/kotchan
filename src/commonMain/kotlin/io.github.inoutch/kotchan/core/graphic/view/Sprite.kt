//package io.github.inoutch.kotchan.core.graphic.view
//
//import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
//import io.github.inoutch.kotchan.utility.type.Vector2
//import io.github.inoutch.kotchan.utility.type.Vector3
//import io.github.inoutch.kotchan.core.tool.TextureFrame
//
//open class Sprite(private val textureAtlas: TextureAtlas) : Square(Vector2(), textureAtlas.texture) {
//
//    fun setAtlas(name: String) {
//        val frame = textureAtlas.frame(name) ?: return
//        val data = textureAtlas.frameMesh(name) ?: return
//        setAtlas(frame, data)
//    }
//
//    fun setAtlas(index: Int) {
//        val frame = textureAtlas.frame(index) ?: return
//        val data = textureAtlas.frameMesh(index) ?: return
//        setAtlas(frame, data)
//    }
//
//    private fun setAtlas(frame: TextureFrame, data: Pair<List<Vector3>, List<Vector2>>) {
//        size = frame.spriteSourceSize.size
//        mesh.updatePositions(data.first)
//        mesh.updateTexcoords(data.second)
//        isPositionsDirty = true
//        isTexcoordsDirty = true
//    }
//}
