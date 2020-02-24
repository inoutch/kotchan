package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.material.Material
/*import io.github.inoutch.kotchan.core.graphic.polygon.Label

class LabelBatch(material: Material) : BatchBase<Label>(material) {
    override val bufferBundle: BatchBufferBundle<Label> = BatchBufferBundle(listOf(
            BatchBufferBundle.BufferInfo(3),
            BatchBufferBundle.BufferInfo(4),
            BatchBufferBundle.BufferInfo(2),
            BatchBufferBundle.BufferInfo(1)
    ))

    override fun size(obj: Label): Int {
        return obj.mesh.size
    }

    override fun update(objectBundle: BatchObjectBufferBundle<Label>) {
        objectBundle.obj.copyPositionsTo(objectBundle.buffers[0])
        objectBundle.obj.copyColorsTo(objectBundle.buffers[1])
        objectBundle.obj.copyTexcoordsTo(objectBundle.buffers[2])
    }
}*/
