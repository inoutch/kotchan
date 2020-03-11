package io.github.inoutch.kotchan.test.utillity.mock

import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.test.utillity.logger.TestLogger

class MockVertexBuffer(val logger: TestLogger?, size: Int, mode: BufferStorageMode) : VertexBuffer(mode) {
    private val nativeIntBuffer = IntArray(size) { 0 }

    private val nativeFloatBuffer = FloatArray(size) { 0.0f }

    override fun copyToBuffer(vertices: IntArray, offset: Int, size: Int) {
        logger?.log(this, "copyToBuffer - offset: $offset, size: $size, vertices: $vertices")
        vertices.copyOfRange(0, size).copyInto(nativeIntBuffer)
    }

    override fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int) {
        vertices.copyOfRange(0, size).copyInto(nativeFloatBuffer)
    }
}
