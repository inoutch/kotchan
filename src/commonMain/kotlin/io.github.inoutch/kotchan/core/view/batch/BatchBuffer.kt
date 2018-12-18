package io.github.inoutch.kotchan.core.view.batch

import io.github.inoutch.kotchan.core.KotchanCore

class BatchBuffer(var maxSize: Int) {

    private val gl = KotchanCore.instance.gl

    private val data: ArrayList<BatchBufferData> = arrayListOf()

    var size: Int = 0
        private set

    val vbo = gl.createVBO(maxSize)

    var isDirty = false

    fun add(vertices: FloatArray): BatchBufferData {
        val last = if (data.size > 0) data.last().end() else 0
        if (last + vertices.size > maxSize) {
            throw RuntimeException("batch: overflow default vbo size")
        }

        val batchBufferData = BatchBufferData(last, vertices)
        gl.updateVBO(vbo, last, vertices)
        this.data.add(batchBufferData)

        size += vertices.size
        return batchBufferData
    }

    fun remove(target: BatchBufferData) {
        size -= target.vertices.size
        data.remove(target)
        isDirty = true
    }

    fun update(batchBufferVertex: BatchBufferData, vertices: FloatArray, autoFlush: Boolean = true) {
        if (vertices.size != batchBufferVertex.vertices.size) {
            throw Error("broken batch buffer vertex")
        }
        batchBufferVertex.vertices = vertices
        if (autoFlush) {
            gl.updateVBO(vbo, batchBufferVertex.start, vertices)
        }
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
        gl.updateVBO(vbo, 0, array)
    }

    fun destroy() {
        vbo.destroy()
    }
}