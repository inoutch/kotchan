package kotchan.view.map.converter

interface TileConverterInterface {
    fun mapId(x: Int, y: Int): Int?
    fun mapId(x: Int, y: Int, mapId: Int)
}