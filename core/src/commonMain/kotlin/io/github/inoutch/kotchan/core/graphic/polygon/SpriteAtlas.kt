package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.core.graphic.texture.TextureFrame
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F

open class SpriteAtlas(
        private val textureAtlas: TextureAtlas,
        size: Vector2F? = null
) : Sprite(size ?: checkNotNull(textureAtlas.frame(0)) { "No frame at texture atlas" }.frame.size) {

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

    private fun setAtlas(frame: TextureFrame, data: Pair<List<Vector3F>, List<Vector2F>>) {
        size = frame.spriteSourceSize.size
        mesh.updatePositions(data.first)
        mesh.updateTexcoords(data.second)
        updatePositionAll()
        updateTexcoordAll()
    }
}
