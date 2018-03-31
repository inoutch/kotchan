package kotchan.view.batch

import kotchan.Engine

class BatchBuffer(var maxSize: Int) {
    private val gl = Engine.getInstance().gl
    private val data: ArrayList<BatchBufferData> = arrayListOf()
    var size: Int = 0
        private set
    val vbo = gl.createVBO(maxSize)
    var isDirty = false

    fun add(vertices: List<Float>): BatchBufferData {
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

    fun update(batchBufferVertex: BatchBufferData, vertices: List<Float>, autoFlush: Boolean = true) {
        if (vertices.size != batchBufferVertex.vertices.size) {
            // TODO: throw exception
            return
        }
        batchBufferVertex.vertices = vertices
        if (autoFlush) {
            gl.updateVBO(vbo, batchBufferVertex.start, vertices)
        }
    }

    fun flush() {
        if (isDirty) {
            gl.updateVBO(vbo, 0, data.flatMap { it.vertices })
            isDirty = false
        }
    }
}