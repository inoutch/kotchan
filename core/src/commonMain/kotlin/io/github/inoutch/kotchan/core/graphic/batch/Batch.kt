package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.material.Material
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

class Batch(material: Material) : BatchBase<Polygon>(material) {
    private val positionBuffer = BatchFloatBuffer()
    private val colorBuffer = BatchFloatBuffer()
    private val texcoordBuffer = BatchFloatBuffer()

    override val bundles = listOf(
            Bundle(3, positionBuffer),
            Bundle(4, colorBuffer),
            Bundle(2, texcoordBuffer)
    )

    init {
        add(bundles.map { it.batchBuffer.vertexBuffer })
    }

    override fun size(obj: Polygon): Int {
        return obj.mesh.size
    }

    override fun update(objectBundle: BatchObjectBufferBundle<Polygon>) {
        val polygon = objectBundle.obj
        polygon.copyPositionsTo(positionBuffer, objectBundle.pointers[0].first)
        polygon.copyColorsTo(colorBuffer, objectBundle.pointers[1].first)
        polygon.copyTexcoordsTo(texcoordBuffer, objectBundle.pointers[2].first)
    }
}
