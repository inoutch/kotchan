package kotchan.view.drawable

import kotchan.view.texture.TextureAtlas
import kotchan.tool.TextureFrame
import utility.type.Vector2
import utility.type.Vector3

open class Sprite(private val textureAtlas: TextureAtlas)
    : Square(Vector2(), textureAtlas.texture) {
    fun setAtlas(name: String) {
        val frame = textureAtlas.frame(name) ?: return
        val data = textureAtlas.frameMesh(name) ?: return
        setAtlas(frame, data)
    }

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