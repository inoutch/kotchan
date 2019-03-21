package io.github.inoutch.kotchan.core.graphic.polygon.tile

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.utility.type.*

class TileMap(val config: Config) : Polygon(Mesh(), config.material) {
    data class Config(
            val material: Material,
            val tileSize: Point,
            val tileTextureSize: Point,
            val mapSize: Point,
            val layerConfig: TileLayer.Config)

    private val layers = config.layerConfig.layers.map { TileLayer(config, it) }.also { addChildren(it) }

    fun layer(layerId: Int) = layers.getOrNull(layerId)
}
