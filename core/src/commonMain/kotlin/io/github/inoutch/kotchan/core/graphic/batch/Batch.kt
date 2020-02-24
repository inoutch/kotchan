package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.material.Material
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

class Batch(material: Material) : BatchBase<Polygon>(material) {
    override val bufferBundle: BatchBufferBundle<Polygon> = BatchBufferBundle()

    override fun size(obj: Polygon): Int {
        return obj.mesh.size
    }

    override fun update(objectBundle: BatchObjectBufferBundle<Polygon>) {
        val polygon = objectBundle.obj
        polygon.copyPositionsTo(objectBundle.buffers[0])
        polygon.copyColorsTo(objectBundle.buffers[1])
        polygon.copyTexcoordsTo(objectBundle.buffers[2])
    }
}
