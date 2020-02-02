package io.github.inoutch.kotchan.core.graphic.compatible.buffer

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic

abstract class VertexBuffer(val mode: BufferStorageMode) : Disposer() {
    companion object {
        fun create(vertices: FloatArray, mode: BufferStorageMode): VertexBuffer {
            return graphic.createVertexBuffer(vertices, mode)
        }
    }

    fun copyToBuffer(vertices: FloatArray, offset: Int) {
        copyToBuffer(vertices, offset, vertices.size - offset)
    }

    abstract fun copyToBuffer(vertices: FloatArray, offset: Int, size: Int)
}
