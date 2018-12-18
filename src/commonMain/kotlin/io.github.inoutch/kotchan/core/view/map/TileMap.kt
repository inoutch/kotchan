package io.github.inoutch.kotchan.core.view.map

open class TileMap(mapInfo: TileMapInfo) : TileMapBase(mapInfo) {

    private val layers: List<TileLayerBase> = mapInfo.layersInfo.map { TileLayer(mapInfo, it) }

    override fun layer(index: Int): TileLayerBase? = layers.getOrNull(index)

    override fun layerSize(): Int = layers.size
}