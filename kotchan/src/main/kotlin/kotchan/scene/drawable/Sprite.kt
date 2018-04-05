package kotchan.scene.drawable

import kotchan.texture.TextureAtlas
import utility.type.Size

class Sprite(private val textureAtlas: TextureAtlas)
    : Square(Size(), textureAtlas.texture) {
    fun setAtlas(name: String) {
        val data = textureAtlas.getFrameMesh(name) ?: return
        mesh.updatePositions(data.first)
        mesh.updateTexcoords(data.second)
        isPositionsDirty = true
        isTexcoordsDirty = true
    }
}