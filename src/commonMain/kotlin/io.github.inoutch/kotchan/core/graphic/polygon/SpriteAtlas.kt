package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.core.tool.TextureFrame
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

open class SpriteAtlas(
        material: Material?,
        private val textureAtlas: TextureAtlas, size: Vector2? = null)
    : Sprite(material, size ?: textureAtlas.frame(0)
        ?.frame?.size ?: throw Error("no frame at texture atlas")) {

    init {
        setAtlas(0)
    }

    fun setAtlas(name: String) {
        val frame = textureAtlas.frame(name) ?: return
        val data = textureAtlas.frameMesh(name) ?: return
        setAtlas(frame, data)
    }

    // not recommended
    fun setAtlas(index: Int) {
        val frame = textureAtlas.frame(index) ?: return
        val data = textureAtlas.frameMesh(index) ?: return
        setAtlas(frame, data)
    }

    private fun setAtlas(frame: TextureFrame, data: Pair<List<Vector3>, List<Vector2>>) {
        size = frame.spriteSourceSize.size
        mesh.updatePositions(data.first)
        mesh.updateTexcoords(data.second)
        isPositionsDirty = true
        isTexcoordsDirty = true
    }
}
