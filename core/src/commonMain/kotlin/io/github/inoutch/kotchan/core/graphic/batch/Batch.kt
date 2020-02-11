package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.material.Material
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

class Batch(private val material: Material) : Disposer() {
    private val bufferBundle = BatchBufferBundle()

    private val polygonBundles = mutableListOf<BatchPolygonBufferBundle>()

    fun add(polygon: Polygon, drawOrder: Int = 0) {
        polygonBundles.add(bufferBundle.allocate(polygon, drawOrder))
    }

    fun render() {
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

    fun sortPositionsByDrawOrder() {
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
