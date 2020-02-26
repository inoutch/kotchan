package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.material.Material
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

class Batch3D(material: Material) : BatchBase<Polygon>(material) {
    private val positionBuffer = BatchFloatBuffer()
    private val colorBuffer = BatchFloatBuffer()
    private val texcoordBuffer = BatchFloatBuffer()
    private val normalBuffer = BatchFloatBuffer()

    override val bundles = listOf(
            Bundle(3, positionBuffer),
            Bundle(4, colorBuffer),
            Bundle(2, texcoordBuffer),
            Bundle(3, normalBuffer)
    )

    override fun update(objectBundle: BatchObjectBufferBundle<Polygon>) {
        objectBundle.obj.copyPositionsTo(positionBuffer, objectBundle.pointers[0].first)
        objectBundle.obj.copyPositionsTo(colorBuffer, objectBundle.pointers[1].first)
        objectBundle.obj.copyPositionsTo(texcoordBuffer, objectBundle.pointers[2].first)
        objectBundle.obj.copyNormalsTo(normalBuffer, objectBundle.pointers[3].first)
    }

    override fun size(obj: Polygon): Int {
        return obj.mesh.size
    }
}
