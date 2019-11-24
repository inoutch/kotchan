package io.github.inoutch.kotchan.core.graphic.batch

data class BatchPolygonBundle(
    val positionBuffer: BatchBuffer,
    val colorBuffer: BatchBuffer,
    val texcoordBuffer: BatchBuffer
) {

    val polygons = mutableListOf<BatchBundle>()

    val size: Int
        get() {
            val p = positionBuffer.size / 3
            val c = colorBuffer.size / 4
            val t = texcoordBuffer.size / 2
            if (p == c && c == t) {
                return p
            }
            throw Error("broken relation of point, color and texcoord.")
        }
}
