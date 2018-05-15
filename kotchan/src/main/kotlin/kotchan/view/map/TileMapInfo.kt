package kotchan.view.map

import interop.graphic.GLTexture
import utility.type.Point
import utility.type.*

open class TileMapInfo(
        val tileSize: Point,
        val texture: GLTexture,
        val layersInfo: List<TileLayerInfo>) {
    val mapSize: Point
    val tileNumber: Point
    val tileTexcoordSize: Vector2

    init {
        if (texture.width % tileSize.x != 0 || texture.height % tileSize.y != 0) {
            throw Error("invalid texture size")
        }
        tileNumber = Point(texture.width / tileSize.x, texture.height / tileSize.y)
        tileTexcoordSize = Vector2(1.0f, 1.0f) / tileNumber
        val x = layersInfo.map { it.mapSize.x }.max() ?: 0.0f
        val y = layersInfo.map { it.mapSize.y }.max() ?: 0.0f
        mapSize = Point(x, y)
    }
}