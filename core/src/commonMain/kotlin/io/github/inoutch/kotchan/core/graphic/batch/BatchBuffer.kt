package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer

interface BatchBuffer {
    companion object {
        const val DEFAULT_SIZE = 1000
    }

    val vertexBuffer: VertexBuffer

    val size: Int

    fun allocate(size: Int): BatchBufferPointer

    fun reallocate(batchBufferPointer: BatchBufferPointer, size: Int)

    fun free(batchBufferPointer: BatchBufferPointer)

    fun sort(scope: (adder: (pointer: BatchBufferPointer) -> Unit) -> Unit)

    fun flush()
}
