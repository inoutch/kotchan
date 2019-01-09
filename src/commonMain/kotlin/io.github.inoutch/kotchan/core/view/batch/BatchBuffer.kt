package io.github.inoutch.kotchan.core.view.batch

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction

class BatchBuffer(defaultSize: Int) : StrictDestruction() {

    private val gl = KotchanCore.instance.gl

    private val data: ArrayList<BatchBufferData> = arrayListOf()

    private var maxSize: Int = defaultSize

    var size: Int = 0
        private set

    var vbo = gl.createVBO(maxSize)
        private set

    var isDirty = false

    fun add(vertices: FloatArray): BatchBufferData {
        val last = if (data.size > 0) data.last().end() else 0
        if (last + vertices.size > maxSize) {
            // reallocate vbo
            isDirty = true
            maxSize *= 2

            gl.deleteVBO(vbo.id)
            vbo = gl.createVBO(maxSize)
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
            isDirty = true
        }
        batchBufferVertex.vertices = vertices
        if (autoFlush && !isDirty) {
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
        size = array.size
    }

    override fun destroy() {
        super.destroy()
        vbo.destroy()
    }
}