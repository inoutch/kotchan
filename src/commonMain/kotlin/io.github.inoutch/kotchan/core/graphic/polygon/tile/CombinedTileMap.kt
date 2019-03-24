package io.github.inoutch.kotchan.core.graphic.polygon.tile

class CombinedTileMap(configs: List<TileMap.Config>) : TileMapBase {
    val tileMaps = configs.map { TileMap(it) }

    private val combinedLayers = tileMaps.map { tileMap ->
        List(tileMap.config.layerConfig.layers.size) { tileMap.layer(it) }.filterNotNull()
    }.flatten()

    override fun layer(layerId: Int) = combinedLayers.getOrNull(layerId)
}
