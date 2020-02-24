package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.graphic.BufferInterface

class BatchObjectBuffer(
    private val batchBuffer: BatchBuffer,
    var batchBufferPointer: BatchBufferPointer
) : BufferInterface<Float> {
    override fun range(first: Int, last: Int) {
        val offset = batchBufferPointer.first
        batchBuffer.range(offset + first, offset + last)
    }

    override fun copy(offset: Int, value: Float) {
        batchBuffer.copy(batchBufferPointer.first + offset, value)
    }

    override fun resize(size: Int) {
        batchBuffer.reallocate(batchBufferPointer, size)
    }
}
