package io.github.inoutch.kotchan.core.graphic.batch

class BatchBufferData(var start: Int, var vertices: FloatArray) {
    fun end() = start + vertices.size
}
