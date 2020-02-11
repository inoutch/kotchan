package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotlin.gl.api.GL_ARRAY_BUFFER
import io.github.inoutch.kotlin.gl.api.gl

class GLVertexBuffer(vertices: FloatArray, mode: BufferStorageMode) : VertexBuffer(mode) {
    val id = gl.genBuffers(1).first()

    init {
        gl.bindBuffer(GL_ARRAY_BUFFER, id)
        gl.bufferData(GL_ARRAY_BUFFER, vertices, mode.glParam)
    }

    override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
        gl.bindBuffer(GL_ARRAY_BUFFER, id)
        gl.bufferSubData(GL_ARRAY_BUFFER, offset.toLong(), vertices, size)
    }
}
