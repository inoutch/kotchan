package kotchan.view.map

import interop.graphic.GLTexture
import utility.type.Vector2

class ChunkTileMapInfo(
        tileSize: Vector2,
        texture: GLTexture,
        val chunkTileLayersInfo: List<ChunkTileLayerInfo>,
        val chunkSize: Vector2,
        val chunkNumber: Vector2,
        val center: Vector2) : TileMapInfo(tileSize, texture, chunkTileLayersInfo)