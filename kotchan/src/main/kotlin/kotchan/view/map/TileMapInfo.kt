package kotchan.view.map

import interop.graphic.GLTexture
import utility.type.Vector2

open class TileMapInfo(
        val name: String,
        val tileSize: Vector2,
        val texture: GLTexture,
        val layersInfo: List<TileLayerInfo>) {
    val mapSize: Vector2
    val tileNumber: Vector2
    val tileTexcoordSize: Vector2

    init {
        if (texture.width % tileSize.x.toInt() != 0 ||
                texture.height % tileSize.y.toInt() != 0) {
            throw Error("invalid texture size")
        }
        tileNumber = Vector2(texture.width / tileSize.x, texture.height / tileSize.y)
        tileTexcoordSize = Vector2(1.0f, 1.0f) / tileNumber
        val x = layersInfo.map { it.mapSize.x }.max() ?: 0.0f
        val y = layersInfo.map { it.mapSize.y }.max() ?: 0.0f
        mapSize = Vector2(x, y)
    }
}