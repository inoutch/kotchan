package kotchan.view.map

import utility.type.Vector2

data class TileLayerInfo(private val mapId: List<List<Int>> = mutableListOf()) {
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

    fun mapId(x: Int, y: Int, defaultValue: Int = 0): Int {
        return mapIdOrigin(x, mapId.size - y - 1, defaultValue)
    }
}