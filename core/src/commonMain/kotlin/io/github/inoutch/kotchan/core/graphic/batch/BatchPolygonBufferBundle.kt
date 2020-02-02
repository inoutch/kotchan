package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

class BatchPolygonBufferBundle(
    val index: Int,
    val polygon: Polygon,
    val positionsPolygonBuffer: BatchPolygonBuffer,
    val colorsPolygonBuffer: BatchPolygonBuffer,
    val texcoordsPolygonBuffer: BatchPolygonBuffer,
    val normalsPolygonBuffer: BatchPolygonBuffer
)
