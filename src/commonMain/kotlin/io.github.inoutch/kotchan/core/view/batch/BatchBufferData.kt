package io.github.inoutch.kotchan.core.view.batch

class BatchBufferData(var start: Int, var vertices: FloatArray) {
    fun end() = start + vertices.size
}