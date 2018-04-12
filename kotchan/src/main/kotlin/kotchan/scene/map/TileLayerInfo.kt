package kotchan.scene.map

data class TileLayerInfo(val mapInfo: TileMapInfo, val mapId: List<List<Int>> = mutableListOf())