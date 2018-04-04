package kotchan.view.batch

import kotchan.view.drawable.Drawable

data class BatchBundle(
        val positionBuffer: BatchBuffer,
        val colorBuffer: BatchBuffer,
        val texcoordBuffer: BatchBuffer) {
    val nodes: MutableMap<Drawable, BatchNode> = mutableMapOf()

    fun getSize(): Int {
        val p = positionBuffer.size / 3
        val c = colorBuffer.size / 4
        val t = texcoordBuffer.size / 2
        if (p == c && c == t) {
            return p
        }
        throw Error("broken relation of position, color and texcoord.")
    }
}