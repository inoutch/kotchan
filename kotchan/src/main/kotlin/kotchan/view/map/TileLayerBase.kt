package kotchan.view.map

import kotchan.view.drawable.Square
import utility.type.Vector2
import kotlin.math.floor

abstract class TileLayerBase(val mapInfo: TileMapInfo) {
    private val bias = Vector2(1.0f, 1.0f) / mapInfo.tileNumber * 0.01f

    abstract fun mapId(x: Int, y: Int, mapId: Int)
    abstract fun mapId(x: Int, y: Int): Int?
    abstract fun fillAll(id: Int)
    abstract fun draw(delta: Float)
    abstract fun destroy()

    protected fun calcTexcoord(id: Int): Vector2 {
        val u = (id % mapInfo.tileNumber.x.toInt()).toFloat()
        val v = floor(id / mapInfo.tileNumber.x)
        return Vector2(u, v) * mapInfo.tileTexcoordSize
    }

    protected fun calcTexcoords(id: Int): List<Vector2> {
        return Square.createSquareTexcoords(calcTexcoord(id) + bias, mapInfo.tileTexcoordSize - bias * 2.0f)
    }

    protected fun calcOffset(x: Int, y: Int, stride: Int): Int {
        return (y * mapInfo.mapSize.x.toInt() + x) * stride
    }
}