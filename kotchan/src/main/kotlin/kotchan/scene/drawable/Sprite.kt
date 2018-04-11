package kotchan.scene.drawable

import kotchan.texture.TextureAtlas
import utility.type.Vector2

open class Sprite(private val textureAtlas: TextureAtlas)
    : Square(Vector2(), textureAtlas.texture) {
    fun setAtlas(name: String) {
        val data = textureAtlas.frameMesh(name) ?: return
        mesh.updatePositions(data.first)
        mesh.updateTexcoords(data.second)
        isPositionsDirty = true
        isTexcoordsDirty = true
    }
}