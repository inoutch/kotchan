package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.polygon.PartialChange
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Vector4

class BatchBuffer(defaultSize: Int) : Disposable {

    var vertexBuffer: VertexBuffer = instance.graphicsApi.allocateVertexBuffer(defaultSize)

    var maxSize = defaultSize
        private set

    var size: Int = 0
        private set

    var isDirty = false

    private val data = arrayListOf<BatchBufferData>()

    override fun dispose() {
        vertexBuffer.dispose()
    }

    fun add(vertices: FloatArray): BatchBufferData {
        val last = if (data.size > 0) data.last().end() else 0
        if (last + vertices.size > maxSize) {
            // reallocate vbo
            isDirty = true
            maxSize = (last + vertices.size) * 2

            vertexBuffer.dispose()
            vertexBuffer = instance.graphicsApi.allocateVertexBuffer(maxSize)
        }

        val batchBufferData = BatchBufferData(last, vertices)

        instance.graphicsApi.copyToBuffer(vertexBuffer, vertices, last)
        this.data.add(batchBufferData)

        size += vertices.size
        return batchBufferData
    }

    fun copy(target: BatchBufferData, vertices: FloatArray) {
        if (vertices.size != target.vertices.size) {
            isDirty = true
        } else {
            instance.graphicsApi.copyToBuffer(vertexBuffer, vertices, target.start)
        }
        target.vertices = vertices
    }

    fun copy(target: BatchBufferData, change: PartialChange<FloatArray>) {
        val changeArray = change.value
        instance.graphicsApi.copyToBuffer(vertexBuffer, changeArray, target.start + change.offset)
        target.vertices = floatArrayOf(
                *target.vertices.sliceArray(IntRange(0, change.offset - 1)),
                *changeArray,
                *target.vertices.sliceArray(IntRange(change.offset + changeArray.size, target.vertices.size - 1)))
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
        instance.graphicsApi.copyToBuffer(vertexBuffer, array, 0)
        size = array.size
    }

    fun remove(target: BatchBufferData) {
        size -= target.vertices.size
        data.remove(target)
        isDirty = true
    }
}
