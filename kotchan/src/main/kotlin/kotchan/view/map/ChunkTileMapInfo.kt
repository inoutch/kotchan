package kotchan.view.map

import interop.graphic.GLTexture
import utility.type.Point
import utility.type.Vector2

class ChunkTileMapInfo(
        tileSize: Point,
        texture: GLTexture,
        val chunkTileLayersInfo: List<ChunkTileLayerInfo>,
        val chunkSize: Point,
        val chunkNumber: Point,
        val center: Vector2) : TileMapInfo(tileSize, texture, chunkTileLayersInfo)