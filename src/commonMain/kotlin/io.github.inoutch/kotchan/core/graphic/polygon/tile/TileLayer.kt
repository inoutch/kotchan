package io.github.inoutch.kotchan.core.graphic.polygon.tile

import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.core.graphic.polygon.Sprite
import io.github.inoutch.kotchan.utility.type.*
import kotlin.math.floor

class TileLayer(private val config: TileMap.Config, layer: Array2D<Int>)
    : Polygon(createMesh(layer, config.tileSize, config.tileTextureSize, config.material.texture.size), config.material) {
    data class Config(val layers: List<Array2D<Int>>)

    companion object {
        fun createMesh(layer: Array2D<Int>, tileSize: Point, textureTileSize: Point, textureSize: Point): Mesh {
            val bundle = layer.getAll().map {
                Sprite.createSquarePositions(it.p.toVector2() * tileSize, tileSize.toVector2()) to
                        calcTexcoords(it.value, calcTileNumber(textureSize, textureTileSize).toPoint(),
                                calcTileTexcoord(textureSize, textureTileSize))
            }.toMap()

            val positions = bundle.keys.flatten()
            val texcoords = bundle.values.flatten()
            return Mesh(positions, texcoords, List(positions.size) { Vector4(1.0f) })
        }

        fun calcTexcoord(id: Int, tileNumber: Point, tileTexcoordSize: Vector2): Vector2 {
            val u = (id % tileNumber.x).toFloat()
            val v = floor((id / tileNumber.x).toFloat())
            return Vector2(u, v) * tileTexcoordSize
        }

        fun calcTexcoords(id: Int, tileNumber: Point, tileTexcoordSize: Vector2): List<Vector2> {
            return Sprite.createSquareTexcoords(calcTexcoord(id, tileNumber, tileTexcoordSize), tileTexcoordSize)
        }

        fun calcTileNumber(textureSize: Point, tileSize: Point): Vector2 {
            val tileNumberX = textureSize.x / tileSize.x
            val tileNumberY = textureSize.y / tileSize.y
            return Vector2(tileNumberX, tileNumberY)
        }

        fun calcTileTexcoord(textureSize: Point, tileSize: Point): Vector2 {
            return Vector2(1.0f, 1.0f) / calcTileNumber(textureSize, tileSize)
        }
    }

    private val data = Array2D(config.mapSize) { layer.get(it) }

    fun getGraphicId(p: Point) = data.get(p)

    fun setGraphicId(p: Point, id: Int) {
        if (getGraphicId(p) == null) {
            return
        }
        data.set(p, id)
        val tileSize = config.tileSize
        val offset = 6 * (p.y * config.mapSize.x + p.x)
        val tileNumber = calcTileNumber(config.material.texture.size, config.tileTextureSize).toPoint()
        val positions = Sprite.createSquarePositions(p.toVector2() * tileSize, tileSize.toVector2())
        val texcoords = calcTexcoords(id, tileNumber, calcTileTexcoord(config.material.texture.size, config.tileTextureSize))
        updatePositions(positions, offset)
        updateTexcoords(texcoords, offset)
    }
}
