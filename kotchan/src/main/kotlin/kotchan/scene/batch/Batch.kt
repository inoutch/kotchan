package kotchan.scene.batch

import interop.graphic.GLAttribLocation
import interop.graphic.GLCamera
import interop.graphic.GLShaderProgram
import kotchan.Engine
import interop.graphic.GLTexture
import kotchan.scene.drawable.Drawable

class Batch {
    companion object {
        var defaultVertexSize = 1024 * 64
    }

    private val gl = Engine.getInstance().gl

    private val nodes: MutableMap<GLShaderProgram, MutableMap<GLTexture, BatchBundle>> = mutableMapOf()
    private val shaders: MutableMap<Drawable, GLShaderProgram> = mutableMapOf()

    fun add(node: Drawable, shaderProgram: GLShaderProgram) {
        shaders[node] = shaderProgram
        var nodesByShader = nodes[shaderProgram]
        if (nodesByShader == null) {
            nodesByShader = mutableMapOf()
            nodes[shaderProgram] = nodesByShader
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
            val positionBufferData = positionBuffer.add(node.positions())
            val texcoordBufferData = texcoordBuffer.add(node.texcoords())
            val colorBufferData = colorBuffer.add(node.colors())
            nodes[node] = BatchNode(positionBufferData, colorBufferData, texcoordBufferData)
        }
    }

    fun remove(node: Drawable) {
        val shaderProgram = shaders[node] ?: return
        val nodesByShaderAndTexture = nodes[shaderProgram]?.get(node.texture) ?: return
        val batchData = nodesByShaderAndTexture.nodes[node] ?: return
        shaders.remove(node)
        nodesByShaderAndTexture.positionBuffer.remove(batchData.positionBufferData)
        nodesByShaderAndTexture.colorBuffer.remove(batchData.colorBufferData)
        nodesByShaderAndTexture.texcoordBuffer.remove(batchData.texcoordBufferData)
        nodesByShaderAndTexture.nodes.remove(node)
    }

    fun draw(delta: Float, camera: GLCamera) {
        var currentShaderProgram: GLShaderProgram? = null
        var currentTexture: GLTexture? = null
        nodes.keys.forEach shader@{ shaderProgram ->
            val nodesByTexture = nodes[shaderProgram] ?: return@shader
            if (currentShaderProgram != shaderProgram) {
                currentShaderProgram = shaderProgram
                shaderProgram.prepare(delta, camera)
                shaderProgram.use()
            }
            nodesByTexture.keys.forEach texture@{ texture ->
                if (currentTexture != texture) {
                    currentTexture = texture
                    texture.use()
                }
                val batchBufferBundle = nodesByTexture[texture] ?: return@texture

                batchBufferBundle.positionBuffer.flush()
                batchBufferBundle.colorBuffer.flush()
                batchBufferBundle.texcoordBuffer.flush()

                batchBufferBundle.nodes
                        .filterKeys { it.isPositionsDirty }
                        .forEach { batchBufferBundle.positionBuffer.update(it.value.positionBufferData, it.key.positions()) }

                batchBufferBundle.nodes
                        .filterKeys { it.isColorsDirty }
                        .forEach { batchBufferBundle.colorBuffer.update(it.value.colorBufferData, it.key.colors()) }

                batchBufferBundle.nodes
                        .filterKeys { it.isTexcoordsDirty }
                        .forEach { batchBufferBundle.texcoordBuffer.update(it.value.texcoordBufferData, it.key.texcoords()) }

                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0, batchBufferBundle.positionBuffer.vbo)
                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0, batchBufferBundle.texcoordBuffer.vbo)
                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, 0, 0, batchBufferBundle.colorBuffer.vbo)

                gl.drawTriangleArrays(0, batchBufferBundle.getSize())
            }
        }
    }

    fun clear() {
        nodes.values.forEach {
            it.values.forEach {
                it.positionBuffer.destroy()
                it.colorBuffer.destroy()
                it.texcoordBuffer.destroy()
            }
        }
        nodes.clear()
        shaders.clear()
    }
}