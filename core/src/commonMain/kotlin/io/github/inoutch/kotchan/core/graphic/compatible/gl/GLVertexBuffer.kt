package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotlin.gl.api.GL_ARRAY_BUFFER
import io.github.inoutch.kotlin.gl.api.gl

class GLVertexBuffer(
    intVertices: IntArray = IntArray(0),
    floatVertices: FloatArray = FloatArray(0),
    mode: BufferStorageMode
) : VertexBuffer(mode) {
    val id = gl.genBuffers(1).first()

    init {
        gl.bindBuffer(GL_ARRAY_BUFFER, id)
        when {
            intVertices.isNotEmpty() -> {
                gl.bufferData(GL_ARRAY_BUFFER, intVertices, mode.glParam)
            }
            floatVertices.isNotEmpty() -> {
                gl.bufferData(GL_ARRAY_BUFFER, floatVertices, mode.glParam)
            }
            else -> throw IllegalStateException("vertices is empty")
        }
    }

    override fun copyToBuffer(vertices: IntArray, offset: Int, size: Int) {
        gl.bindBuffer(GL_ARRAY_BUFFER, id)
        gl.bufferSubData(GL_ARRAY_BUFFER, offset.toLong(), vertices, size)
    }

    override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
        gl.bindBuffer(GL_ARRAY_BUFFER, id)
        gl.bufferSubData(GL_ARRAY_BUFFER, offset.toLong(), vertices, size)
    }
}
