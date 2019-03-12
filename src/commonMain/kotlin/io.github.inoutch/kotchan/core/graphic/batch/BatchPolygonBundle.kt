package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.polygon.Polygon

data class BatchBundle(
        val positionBuffer: BatchBuffer,
        val colorBuffer: BatchBuffer,
        val texcoordBuffer: BatchBuffer) {

    val drawables: MutableMap<Polygon, BatchNode> = mutableMapOf()

    fun getSize(): Int {
        val p = positionBuffer.size / 3
        val c = colorBuffer.size / 4
        val t = texcoordBuffer.size / 2
        if (p == c && c == t) {
            return p
        }
        throw Error("broken relation of point, color and texcoord.")
    }
}
