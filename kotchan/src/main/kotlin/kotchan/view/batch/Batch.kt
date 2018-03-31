package kotchan.view.batch

import interop.graphic.GLAttribLocation
import interop.graphic.GLShaderProgram
import kotchan.Engine
import kotchan.view.Texture
import kotchan.view.drawable.Drawable

class Batch {
    companion object {
        var defaultVertexSize = 1024 * 64
    }

    private val gl = Engine.getInstance().gl

    private val nodes: MutableMap<GLShaderProgram, MutableMap<Texture, BatchBundle>> = mutableMapOf()

    fun add(node: Drawable) {
        var nodesByShader = nodes[node.shaderProgram]
        if (nodesByShader == null) {
            nodesByShader = mutableMapOf()
            nodes[node.shaderProgram] = nodesByShader
        }
        var nodesByShaderAndTexture = nodesByShader[node.texture]
        if (nodesByShaderAndTexture == null) {
            nodesByShaderAndTexture = BatchBundle(
                    BatchBuffer(defaultVertexSize * 3),
                    BatchBuffer(defaultVertexSize * 4),
                    BatchBuffer(defaultVertexSize * 2))
            nodesByShader[node.texture] = nodesByShaderAndTexture
        }
        nodesByShaderAndTexture.apply {
            val positionBufferData = positionBuffer.add(node.mesh.vertices)
            val colorBufferData = colorBuffer.add(node.mesh.colors)
            val texcoordBufferData = texcoordBuffer.add(node.mesh.texcoords)
            nodes[node] = BatchNode(positionBufferData, colorBufferData, texcoordBufferData)
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

    fun draw(delta: Float) {
        var currentShaderProgram: GLShaderProgram? = null
        var currentTexture: Texture? = null
        nodes.keys.forEach shader@{ shaderProgram ->
            val nodesByTexture = nodes[shaderProgram] ?: return@shader
            if (currentShaderProgram != shaderProgram) {
                currentShaderProgram = shaderProgram
                shaderProgram.use()
                // TODO: set uniform
            }
            nodesByTexture.keys.forEach texture@{ texture ->
                if (currentTexture != texture) {
                    currentTexture = texture
                    // TODO: enable texture and change dynamic index
                    texture.use(0)
                }
                val batchBufferBundle = nodesByTexture[texture] ?: return@texture

                batchBufferBundle.positionBuffer.flush()
                batchBufferBundle.colorBuffer.flush()
                batchBufferBundle.texcoordBuffer.flush()

                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, batchBufferBundle.positionBuffer.vbo)
                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, batchBufferBundle.texcoordBuffer.vbo)
                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, batchBufferBundle.colorBuffer.vbo)

                gl.drawTriangleArrays(0, batchBufferBundle.getSize())
            }
        }
    }
}