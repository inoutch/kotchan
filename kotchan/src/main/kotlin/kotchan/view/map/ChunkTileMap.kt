package kotchan.view.map

import utility.type.Point
import utility.type.Vector2
import kotlin.math.floor

fun createChunkLayer(
        mapInfo: ChunkTileMapInfo,
        mapIdGetter: (layer: Int, x: Int, y: Int) -> Int): List<ChunkTileLayer> {
    return mapInfo.chunkTileLayersInfo.mapIndexed { index, chunkTileLayerInfo ->
        ChunkTileLayer(mapInfo, chunkTileLayerInfo) { x, y -> mapIdGetter(index, x, y) }
    }
}

class ChunkTileMap(
        val chunkTileMapInfo: ChunkTileMapInfo,
        mapIdGetter: (layer: Int, x: Int, y: Int) -> Int) : TileMapBase(chunkTileMapInfo) {
    companion object {
        fun calcMapPosition(center: Vector2, mapInfo: ChunkTileMapInfo): Point {
            val x = center.x / (mapInfo.chunkSize.x * mapInfo.tileSize.x)
            val y = center.y / (mapInfo.chunkSize.y * mapInfo.tileSize.y)
            return Point(if (x > 0) x.toInt() else floor(x).toInt(), if (y > 0) y.toInt() else floor(y).toInt())
        }
    }

    private val layers = createChunkLayer(chunkTileMapInfo, mapIdGetter)
    private var beforeCenter = calcMapPosition(chunkTileMapInfo.center, chunkTileMapInfo)

    override fun layer(index: Int): TileLayerBase? = layers.getOrNull(index)
    override fun layerSize(): Int = layers.size

    fun updateCenter(center: Vector2) {
        val (ax, ay) = calcMapPosition(center, chunkTileMapInfo)
        val (bx, by) = beforeCenter
        if (ax != bx || ay != by) {
            layers.forEach { it.updateVertices(bx, by, ax - bx, ay - by) }
            beforeCenter = Point(ax, ay)
        }
    }

}