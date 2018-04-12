package kotchan.scene.map

import interop.graphic.GLTexture
import utility.type.Vector2

data class TileMapInfo(
        val name: String,
        val tileSize: Vector2,
        val size: Vector2,
        val texture: GLTexture)