package kotchan.view.map

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
    private val layers = createChunkLayer(chunkTileMapInfo, mapIdGetter)
    private var beforeCenter = calcMapPosition(chunkTileMapInfo.center)

    override fun layer(index: Int): TileLayerBase? = layers.getOrNull(index)
    override fun layerSize(): Int = layers.size

    fun updateCenter(center: Vector2) {
        val (ax, ay) = calcMapPosition(center)
        val (bx, by) = beforeCenter
        if (ax != bx || ay != by) {
            layers.forEach { it.updateVertices(bx, by, ax - bx, ay - by) }
            beforeCenter = ax to ay
        }
    }

    private fun calcMapPosition(center: Vector2): Pair<Int, Int> {
        val x = center.x / (chunkTileMapInfo.chunkSize.x * chunkTileMapInfo.tileSize.x)
        val y = center.y / (chunkTileMapInfo.chunkSize.y * chunkTileMapInfo.tileSize.y)
        return (if (x > 0) x.toInt() else floor(x).toInt()) to (if (y > 0) y.toInt() else floor(y).toInt())
    }
}