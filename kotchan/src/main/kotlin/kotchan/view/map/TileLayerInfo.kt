package kotchan.view.map

import utility.type.Vector2

open class TileLayerInfo(private val mapId: List<List<Int>> = mutableListOf()) {
    companion object {
        fun createEmpty(layer: Int, width: Int, height: Int, defaultValue: Int = 0): List<TileLayerInfo> {
            return List(layer) { TileLayerInfo(List(height) { List(width) { defaultValue } }) }
        }

        fun createEmpty(layer: Int, width: Int, height: Int, defaultValue: (layer: Int) -> Int): List<TileLayerInfo> {
            return List(layer) { layerIndex -> TileLayerInfo(List(height) { List(width) { defaultValue(layerIndex) } }) }
        }
    }

    val mapSize: Vector2 = Vector2(mapId.map { it.size }.max()?.toFloat() ?: 0.0f, mapId.size.toFloat())
    var visible = true

    fun mapIdOrigin(x: Int, y: Int, defaultValue: Int = 0): Int {
        if (y < 0 || mapId.size <= y) {
            return defaultValue
        }
        if (x < 0 || mapId[y].size <= x) {
            return defaultValue
        }
        return mapId[y][x]
    }

    open fun mapId(x: Int, y: Int, defaultValue: Int = 0): Int {
        return mapIdOrigin(x, mapId.size - y - 1, defaultValue)
    }
}