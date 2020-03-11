package io.github.inoutch.kotchan.test.utillity.mock

import io.github.inoutch.kotchan.core.graphic.batch.BatchBuffer
import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferPointer
import io.github.inoutch.kotchan.core.graphic.batch.BatchFloatBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.test.utillity.logger.TestLogger

class MockBatchBuffer(val testLogger: TestLogger) : BatchBuffer {
    private val buffer = BatchFloatBuffer(1000) { vertexBuffer }

    override val vertexBuffer: VertexBuffer
        get() = MockVertexBuffer(testLogger, 1000, BufferStorageMode.Dynamic)

    override val size: Int
        get() = 1000

    override fun allocate(size: Int): BatchBufferPointer {
        testLogger.log(this, "allocate: $size")
        return buffer.allocate(size)
    }

    override fun reallocate(batchBufferPointer: BatchBufferPointer, size: Int) {
        testLogger.log(this, "reallocate: ${batchBufferPointer.first}, ${batchBufferPointer.size}, $size")
        return buffer.reallocate(batchBufferPointer, size)
    }

    override fun free(batchBufferPointer: BatchBufferPointer) {
        testLogger.log(this, "free: ${batchBufferPointer.first}, ${batchBufferPointer.size}")
        return buffer.free(batchBufferPointer)
    }

    override fun sort(scope: (adder: (pointer: BatchBufferPointer) -> Unit) -> Unit) {
        testLogger.log(this, "sort")
        val adder = { pointer: BatchBufferPointer ->
            testLogger.log(this, "adder: ${pointer.first}, ${pointer.size}")
        }
        scope(adder)
        return buffer.sort(scope)
    }

    override fun flush() {
        testLogger.log(this, "flush")
        return buffer.flush()
    }
}
