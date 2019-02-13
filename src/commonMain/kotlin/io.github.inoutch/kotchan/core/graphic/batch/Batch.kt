package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.utility.graphic.GLAttribLocation
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.utility.graphic.GLShaderProgram
import io.github.inoutch.kotchan.utility.graphic.GLTexture
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction
import io.github.inoutch.kotchan.core.graphic.view.View
import io.github.inoutch.kotchan.core.graphic.view.ViewBase

class Batch : StrictDestruction() {
    companion object {
        var defaultVertexSize = 1024 * 64
    }

    private val gl = KotchanCore.instance.gl

    private val bundles: MutableMap<GLShaderProgram, MutableMap<GLTexture, BatchBundle>> = mutableMapOf()

    private val shaders: MutableMap<View, GLShaderProgram> = mutableMapOf()

    private val views: MutableList<View> = mutableListOf()

    fun add(view: View, shaderProgram: GLShaderProgram) = addDeep(view, shaderProgram)

    fun addViewBase(viewBase: ViewBase, shaderProgram: GLShaderProgram) {
        viewBase.viewChildren.forEach { addShallow(it, shaderProgram) }
    }

    fun addShallow(view: View, shaderProgram: GLShaderProgram) {
        shaders[view] = shaderProgram
        views.add(view)

        var nodesByShader = bundles[shaderProgram]
        if (nodesByShader == null) {
            nodesByShader = mutableMapOf()
            bundles[shaderProgram] = nodesByShader
        }

        var nodesByShaderAndTexture = nodesByShader[view.texture]
        if (nodesByShaderAndTexture == null) {
            nodesByShaderAndTexture = BatchBundle(
                    BatchBuffer(defaultVertexSize * 3),
                    BatchBuffer(defaultVertexSize * 4),
                    BatchBuffer(defaultVertexSize * 2))
            nodesByShader[view.texture] = nodesByShaderAndTexture
        }

        nodesByShaderAndTexture.apply {
            val positionBufferData = positionBuffer.add(view.positions())
            val texcoordBufferData = texcoordBuffer.add(view.texcoords())
            val colorBufferData = colorBuffer.add(view.colors())
            drawables[view] = BatchNode(positionBufferData, colorBufferData, texcoordBufferData)
        }
    }

    fun addDeep(view: View, shaderProgram: GLShaderProgram) {
        addShallow(view, shaderProgram)
        addViewBase(view, shaderProgram)
    }

    fun remove(view: View) {
        views.remove(view)

        val shaderProgram = shaders[view] ?: return
        val nodesByShaderAndTexture = bundles[shaderProgram]?.get(view.texture) ?: return
        val batchData = nodesByShaderAndTexture.drawables[view] ?: return

        shaders.remove(view)

        nodesByShaderAndTexture.positionBuffer.remove(batchData.positionBufferData)
        nodesByShaderAndTexture.colorBuffer.remove(batchData.colorBufferData)
        nodesByShaderAndTexture.texcoordBuffer.remove(batchData.texcoordBufferData)
        nodesByShaderAndTexture.drawables.remove(view)
    }

    fun draw(delta: Float, camera: Camera) {
        var currentShaderProgram: GLShaderProgram? = null
        var currentTexture: GLTexture? = null
        var currentTextureEnable = true

        bundles.keys.forEach shader@{ shaderProgram ->
            val nodesByTexture = bundles[shaderProgram] ?: return@shader
            if (currentShaderProgram != shaderProgram) {
                currentShaderProgram = shaderProgram
                shaderProgram.use()
                shaderProgram.prepare(delta, camera.combine)
            }
            nodesByTexture.keys.forEach texture@{ texture ->
                if (currentTexture != texture) {
                    currentTexture = texture
                    texture.use()
                    val enable = texture != GLTexture.empty
                    if (enable != currentTextureEnable) {
                        shaderProgram.textureEnable = enable
                        currentTextureEnable = enable
                        shaderProgram.prepare(delta, camera.combine)
                    }
                }
                val batchBufferBundle = nodesByTexture[texture] ?: return@texture

                batchBufferBundle.positionBuffer.flush()
                batchBufferBundle.colorBuffer.flush()
                batchBufferBundle.texcoordBuffer.flush()

                batchBufferBundle.drawables
                        .filterKeys { it.isPositionsDirty }
                        .forEach { batchBufferBundle.positionBuffer.update(it.value.positionBufferData, it.key.positions()) }

                batchBufferBundle.drawables
                        .filterKeys { it.isColorsDirty }
                        .forEach { batchBufferBundle.colorBuffer.update(it.value.colorBufferData, it.key.colors()) }

                batchBufferBundle.drawables
                        .filterKeys { it.isTexcoordsDirty }
                        .forEach { batchBufferBundle.texcoordBuffer.update(it.value.texcoordBufferData, it.key.texcoords()) }

                gl.bindVBO(batchBufferBundle.positionBuffer.vbo.id)
                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
                gl.bindVBO(batchBufferBundle.texcoordBuffer.vbo.id)
                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
                gl.bindVBO(batchBufferBundle.colorBuffer.vbo.id)
                gl.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, 0, 0)

                gl.drawTriangleArrays(0, batchBufferBundle.getSize())
            }
        }
    }

    override fun destroy() {
        super.destroy()

        bundles.values.forEach { node ->
            node.values.forEach {
                it.positionBuffer.destroy()
                it.colorBuffer.destroy()
                it.texcoordBuffer.destroy()
            }
        }
        bundles.clear()
        shaders.clear()
    }
}
