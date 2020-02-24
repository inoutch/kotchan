package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.material.Material
import io.github.inoutch.kotchan.core.graphic.polygon.LabelSprite

class LabelBatch(material: Material) : BatchBase<LabelSprite>(material) {
    private val positionBuffer = BatchFloatBuffer()
    private val colorBuffer = BatchFloatBuffer()
    private val texcoordBuffer = BatchFloatBuffer()
    private val texNumbersBuffer = BatchFloatBuffer()

    override val bundles: List<Bundle> = listOf(
            Bundle(3, positionBuffer),
            Bundle(4, colorBuffer),
            Bundle(2, texcoordBuffer),
            Bundle(1, texNumbersBuffer)
    )

    override fun size(obj: LabelSprite): Int {
        return obj.mesh.size
    }

    override fun update(objectBundle: BatchObjectBufferBundle<LabelSprite>) {
        objectBundle.obj.copyPositionsTo(positionBuffer, objectBundle.pointers[0].first)
        objectBundle.obj.copyColorsTo(colorBuffer, objectBundle.pointers[1].first)
        objectBundle.obj.copyTexcoordsTo(texcoordBuffer, objectBundle.pointers[2].first)
        objectBundle.obj.copyTexNumbersTo(texNumbersBuffer, objectBundle.pointers[3].first)
    }
}
