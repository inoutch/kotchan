package io.github.inoutch.kotchan.core.graphic.polygon.tile

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.utility.type.*

interface TileMapBase {
    fun layer(layerId: Int): TileLayer?
}

class TileMap(val config: Config) : Polygon(Mesh(), config.material), TileMapBase {
    data class Config(
            val material: Material,
            val tileSize: Point,
            val tileTextureSize: Point,
            val mapSize: Point,
            val layerConfig: TileLayer.Config,
            val biasPerPixel: Float = 0.2f)

    private val layers = config.layerConfig.layers.map { TileLayer(config, it) }.also { addChildren(it) }

    override fun layer(layerId: Int) = layers.getOrNull(layerId)
}
