package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.Camera
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.core.graphic.compatible.Material

class Batch(private val material: Material): Disposer() {
    private val bufferBundle = BatchBufferBundle()

    private val polygonBundles = mutableListOf<BatchPolygonBufferBundle>()

    fun add(polygon: Polygon, index: Int = 0) {
        polygonBundles.add(bufferBundle.allocate(polygon, index))
    }

    fun render(delta: Float, camera: Camera) {
        material.bind()

        var i = 0
        while (i < polygonBundles.size) {
            bufferBundle.update(polygonBundles[i])
            i++
        }
        bufferBundle.positionBuffer.flush()
        bufferBundle.colorBuffer.flush()
        bufferBundle.texcoordBuffer.flush()
        bufferBundle.normalBuffer.flush()
        graphic.drawTriangles(bufferBundle)
    }

    fun sortPositions() {
        polygonBundles.sortBy { it.index }
        bufferBundle.positionBuffer.sort {
            var i = 0
            while (i < polygonBundles.size) {
                it.invoke(polygonBundles[i].positionsPolygonBuffer.batchBufferPointer)
                i++
            }
        }
    }
}
