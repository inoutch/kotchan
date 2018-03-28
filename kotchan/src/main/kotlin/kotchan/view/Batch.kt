package kotchan.view

import interop.graphic.GLAttribLocation
import interop.graphic.GLShaderProgram
import kotchan.Engine
import kotchan.view.drawable.Drawable

data class BatchData(
        val positionBufferData: BatchBufferData,
        val colorBufferData: BatchBufferData,
        val texcoordBufferData: BatchBufferData)

data class BatchBufferData(val start: Int, var vertices: List<Float>) {
    fun end() = start + vertices.size
}

class BatchBuffer(var maxSize: Int) {
    private val gl = Engine.getInstance().gl
    private val vbo = gl.createVBO(maxSize)
    private val data: ArrayList<BatchBufferData> = arrayListOf()
    var isDirty = false

    fun add(vertices: List<Float>): BatchBufferData {
        val last = if (data.size > 0) data.last().end() else 0
        if (last + vertices.size > maxSize) {
            throw RuntimeException("batch: overflow default vbo size")
        }

        val batchBufferData = BatchBufferData(last, vertices)
        gl.updateVBO(vbo, last, vertices)
        this.data.add(batchBufferData)

        return batchBufferData
    }

    fun remove(target: BatchBufferData) {
        data.remove(target)
        isDirty = true
    }

    fun update(batchBufferData: BatchBufferData, vertices: List<Float>, autoFlush: Boolean = true) {
        if (vertices.size != batchBufferData.vertices.size) {
            // TODO: throw exception
            return
        }
        batchBufferData.vertices = vertices
        if (autoFlush) {
            gl.updateVBO(vbo, batchBufferData.start, vertices)
        }
    }

    fun flush() {
        if (isDirty) {
            gl.updateVBO(vbo, 0, data.flatMap { it.vertices })
            isDirty = false
        }
    }
}

data class BatchBufferBundle(
        val positionBuffer: BatchBuffer,
        val colorBuffer: BatchBuffer,
        val texcoordBuffer: BatchBuffer) {
    val nodes: MutableMap<Drawable, BatchData> = mutableMapOf()
}

class Batch {
    companion object {
        var defaultVertexSize = 1024 * 64
    }

    private val gl = Engine.getInstance().gl

    private val nodes: MutableMap<GLShaderProgram, MutableMap<Texture, BatchBufferBundle>> = mutableMapOf()

    fun add(node: Drawable) {
        // TODO: throw exception
        val mesh = node.mesh ?: return

        var nodesByShader = nodes[node.shaderProgram]
        if (nodesByShader == null) {
            nodesByShader = mutableMapOf()
        }
        var nodesByShaderAndTexture = nodesByShader[node.texture]
        if (nodesByShaderAndTexture == null) {
            nodesByShaderAndTexture = BatchBufferBundle(
                    BatchBuffer(defaultVertexSize * 3),
                    BatchBuffer(defaultVertexSize * 4),
                    BatchBuffer(defaultVertexSize * 2))
        }
        nodesByShaderAndTexture.apply {
            val batchData = BatchData(
                    positionBuffer.add(mesh.vertices),
                    colorBuffer.add(mesh.colors),
                    texcoordBuffer.add(mesh.texcoords))
            nodes[node] = batchData
        }
    }

    fun remove(node: Drawable) {
        val nodesByShaderAndTexture = nodes[node.shaderProgram]?.get(node.texture) ?: return
        val batchData = nodesByShaderAndTexture.nodes[node] ?: return
        nodesByShaderAndTexture.positionBuffer.remove(batchData.positionBufferData)
        nodesByShaderAndTexture.colorBuffer.remove(batchData.colorBufferData)
        nodesByShaderAndTexture.texcoordBuffer.remove(batchData.texcoordBufferData)
        nodesByShaderAndTexture.nodes.remove(node)
    }

    fun flush(delta: Float) {
        var currentShaderProgram: GLShaderProgram? = null
        var currentTexture: Texture? = null
        nodes.keys.forEach shader@{ shaderProgram ->
            val nodesByTexture = nodes[shaderProgram] ?: return@shader
            nodesByTexture.keys.forEach texture@{ texture ->
                val batchBufferBundle = nodesByTexture[texture] ?: return@texture

                batchBufferBundle.positionBuffer.flush()
                batchBufferBundle.colorBuffer.flush()
                batchBufferBundle.texcoordBuffer.flush()
                // gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, vertices)
                // gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, texcoords)
                // gl.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, colors)
                //gl.drawTriangleArrays(0, batchBufferBundle.positionBuffer. size / 3)
            }
        }
    }

    private fun render(node: Drawable) {

    }
}