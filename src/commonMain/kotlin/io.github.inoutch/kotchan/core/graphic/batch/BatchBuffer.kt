package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.compatible.Buffer
import io.github.inoutch.kotchan.utility.Disposable

class BatchBuffer(defaultSize: Int) : Disposable {

    var buffer: Buffer = instance.graphicsApi.allocateBuffer(defaultSize)

    var maxSize = defaultSize
        private set

    var size: Int = 0
        private set

    var isDirty = false

    private val data = arrayListOf<BatchBufferData>()

    override fun dispose() {
        buffer.dispose()
    }

    fun add(vertices: FloatArray): BatchBufferData {
        val last = if (data.size > 0) data.last().end() else 0
        if (last + vertices.size > maxSize) {
            // reallocate vbo
            isDirty = true
            maxSize = (last + vertices.size) * 2

            buffer.dispose()
            buffer = instance.graphicsApi.allocateBuffer(maxSize)
        }

        val batchBufferData = BatchBufferData(last, vertices)

        instance.graphicsApi.copyToBuffer(buffer, vertices, last)
        this.data.add(batchBufferData)

        size += vertices.size
        return batchBufferData
    }

    fun copy(target: BatchBufferData, vertices: FloatArray) {
        if (vertices.size != target.vertices.size) {
            isDirty = true
        }
        target.vertices = vertices
    }

    fun flush() {
        if (!isDirty) {
            return
        }
        isDirty = false

        var currentPoint = 0
        val array = data
                .flatMap {
                    it.start = currentPoint
                    currentPoint += it.vertices.size
                    it.vertices.asIterable()
                }
                .toFloatArray()
        instance.graphicsApi.copyToBuffer(buffer, array, 0)
        size = array.size
    }

    fun remove(target: BatchBufferData) {
        size -= target.vertices.size
        data.remove(target)
        isDirty = true
    }
}
