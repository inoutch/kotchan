package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

class BatchBufferBundle(
    val positionBuffer: BatchBuffer = BatchBuffer(),
    val colorBuffer: BatchBuffer = BatchBuffer(),
    val texcoordBuffer: BatchBuffer = BatchBuffer(),
    val normalBuffer: BatchBuffer = BatchBuffer()
) {
    companion object {
        const val POSITION_SIZE = 3
        const val COLOR_SIZE = 4
        const val TEXCOORD_SIZE = 2
        const val NORMAL_SIZE = 3
    }

    fun allocate(polygon: Polygon, index: Int): BatchPolygonBufferBundle {
        val posData = positionBuffer.allocate(polygon.mesh.size * POSITION_SIZE)
        val colData = colorBuffer.allocate(polygon.mesh.size * COLOR_SIZE)
        val texData = texcoordBuffer.allocate(polygon.mesh.size * TEXCOORD_SIZE)
        val nomData = normalBuffer.allocate(polygon.mesh.size * NORMAL_SIZE)
        return BatchPolygonBufferBundle(
                index,
                polygon,
                BatchPolygonBuffer(positionBuffer, posData),
                BatchPolygonBuffer(colorBuffer, colData),
                BatchPolygonBuffer(texcoordBuffer, texData),
                BatchPolygonBuffer(normalBuffer, nomData)
        )
    }

    fun update(polygonBufferBundle: BatchPolygonBufferBundle) {
        val polygon = polygonBufferBundle.polygon
        polygon.copyPositionsTo(polygonBufferBundle.positionsPolygonBuffer)
        polygon.copyColorsTo(polygonBufferBundle.colorsPolygonBuffer)
        polygon.copyTexcoordsTo(polygonBufferBundle.texcoordsPolygonBuffer)
        polygon.copyNormalsTo(polygonBufferBundle.normalsPolygonBuffer)
    }
}
