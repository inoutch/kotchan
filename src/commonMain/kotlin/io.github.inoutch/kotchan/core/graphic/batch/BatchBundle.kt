package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

data class BatchBundle(
    val polygon: Polygon,
    val positionBufferData: BatchBufferData,
    val colorBufferData: BatchBufferData,
    val texcoordBufferData: BatchBufferData
)
