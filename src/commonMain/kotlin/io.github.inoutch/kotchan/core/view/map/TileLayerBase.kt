package io.github.inoutch.kotchan.core.view.map

import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.core.view.drawable.Square
import kotlin.math.floor

abstract class TileLayerBase(val mapInfo: TileMapInfo) {

    private val bias = Vector2(1.0f, 1.0f) / mapInfo.tileNumber * 0.01f

    abstract fun fillAll(id: Int)

    abstract fun draw(delta: Float)

    abstract fun destroy()

    abstract fun mapId(point: Point, id: Int)

    abstract fun mapId(point: Point): Int?

    protected fun calcTexcoord(id: Int): Vector2 {
        val u = (id % mapInfo.tileNumber.x).toFloat()
        val v = floor((id / mapInfo.tileNumber.x).toFloat())
        return Vector2(u, v) * mapInfo.tileTexcoordSize
    }

    protected fun calcTexcoords(id: Int): List<Vector2> {
        return Square.createSquareTexcoords(calcTexcoord(id) + bias, mapInfo.tileTexcoordSize - bias * 2.0f)
    }

    protected fun calcOffset(x: Int, y: Int, stride: Int): Int {
        return (y * mapInfo.mapSize.x + x) * stride
    }
}