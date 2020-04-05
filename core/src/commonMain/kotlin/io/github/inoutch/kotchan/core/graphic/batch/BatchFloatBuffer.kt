package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.BufferInterface
import io.github.inoutch.kotchan.core.graphic.ChangeRange
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer

class BatchFloatBuffer(
    defaultSize: Int = DEFAULT_SIZE,
    private val creator: (vertices: FloatArray) -> VertexBuffer = { VertexBuffer.create(it, BufferStorageMode.Dynamic) }
) : Disposer(), BatchBuffer, BufferInterface<Float> {
    companion object {
        const val DEFAULT_SIZE = 1000
    }

    var vertices = FloatArray(defaultSize)
        private set

    override var vertexBuffer = creator(vertices)
        private set

    override var size: Int = 0
        private set

    private val pointers = arrayListOf<BatchBufferPointer>()

    private val changeRange = ChangeRange(size)

    init {
        add(vertexBuffer)
    }

    override fun allocate(size: Int): BatchBufferPointer {
        val last = last()
        this.size += size
        if (last + size > vertices.size) {
            reallocateVertexBuffer(last + size)
        }
        return BatchBufferPointer(last, size)
                .also { pointers.add(it) }
    }

    override fun reallocate(batchBufferPointer: BatchBufferPointer, size: Int) {
        val diff = size - batchBufferPointer.size
        val newSize = last() + diff
        val oldVertices = vertices.copyOf()
        changeRange.resizeAndUpdate(newSize, batchBufferPointer.first)

        if (vertices.size < newSize) {
            // Need reallocate vertex buffer
            reallocateVertexBuffer(newSize)
        }

        val last = batchBufferPointer.last()
        val newLast = last + diff
        var i = 0

        // Copy subsequent vertices
        while (i < newSize - newLast) {
            vertices[i + newLast] = oldVertices[i + last]
            i++
        }
        batchBufferPointer.size = size
        var afterIndex = pointers.indexOf(batchBufferPointer) + 1
        var prevPointer = batchBufferPointer
        while (afterIndex < pointers.size) {
            val pointer = pointers[afterIndex]
            pointer.first = prevPointer.last()
            prevPointer = pointer
            afterIndex++
        }
        this.size = newSize
    }

    override fun free(batchBufferPointer: BatchBufferPointer) {
        reallocate(batchBufferPointer, 0)
        pointers.remove(batchBufferPointer)
    }

    override fun sort(scope: (adder: (pointer: BatchBufferPointer) -> Unit) -> Unit) {
        if (this.pointers.isEmpty()) {
            return
        }
        val data = arrayListOf<BatchBufferPointer>()
        var size = 0
        val adder = { pointer: BatchBufferPointer ->
            data.add(pointer)
            size += pointer.size
            Unit
        }
        scope(adder)
        check(size == this.pointers.last().last()) {
            "Invalid number of adder calls"
        }
        this.pointers.clear()
        this.pointers.addAll(data)

        val oldVertices = vertices.copyOf()
        var i = 0
        size = 0
        while (i < data.size) {
            val pointer = pointers[i]
            val nextFirst = size
            var j = 0
            while (j < pointer.size) {
                vertices[nextFirst + j] = oldVertices[pointer.first + j]
                j++
            }
            pointer.first = nextFirst
            size += pointer.size
            i++
        }
        range(0, size)
        changeRange.resize(size)
    }

    override fun resize(size: Int) {
        reallocate(pointers.first(), size)
    }

    override fun copy(offset: Int, value: Float) {
        vertices[offset] = value
    }

    override fun range(first: Int, last: Int) {
        changeRange.update(first, last)
    }

    override fun flush() {
        val change = changeRange.change ?: return
        val size = change.last - change.first
        if (size > 0) {
            vertexBuffer.copyToBuffer(vertices.sliceArray(change.first until change.last), change.first, size)
        }
        changeRange.reset()
    }

    private fun reallocateVertexBuffer(size: Int) {
        changeRange.resize(size)
        changeRange.updateAll()
        dispose(vertexBuffer)

        val newVertexSize = size * 2
        vertices = FloatArray(newVertexSize) {
            if (vertices.size > it) {
                vertices[it]
            } else 0.0f
        }
        vertexBuffer = creator(vertices)
        this.size = size
    }

    private fun last(): Int {
        return if (pointers.size > 0) pointers.last().last() else 0
    }
}
