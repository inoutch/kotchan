package io.github.inoutch.kotchan.core.graphic.map

import io.github.inoutch.kotchan.utility.graphic.GLTexture
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector2

class ChunkTileMapInfo(
        tileSize: Point,
        texture: GLTexture,
        val chunkTileLayersInfo: List<ChunkTileLayerInfo>,
        val chunkSize: Point,
        val chunkNumber: Point,
        val center: Vector2) : TileMapInfo(tileSize, texture, chunkTileLayersInfo)
