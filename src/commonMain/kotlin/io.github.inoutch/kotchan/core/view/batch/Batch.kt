package io.github.inoutch.kotchan.core.view.batch

import io.github.inoutch.kotchan.utility.graphic.GLAttribLocation
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.utility.graphic.GLShaderProgram
import io.github.inoutch.kotchan.utility.graphic.GLTexture
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.view.drawable.Drawable
import io.github.inoutch.kotchan.core.view.drawable.DrawableGroup

class Batch {
    companion object {
        var defaultVertexSize = 1024 * 64
    }

    private val gl = KotchanCore.instance.gl

    private val drawables: MutableMap<GLShaderProgram, MutableMap<GLTexture, BatchBundle>> = mutableMapOf()

    private val groups: MutableList<DrawableGroup> = mutableListOf()

    private val shaders: MutableMap<Drawable, GLShaderProgram> = mutableMapOf()

    fun add(drawable: Drawable, shaderProgram: GLShaderProgram) {
        shaders[drawable] = shaderProgram
        var nodesByShader = drawables[shaderProgram]
        if (nodesByShader == null) {
            nodesByShader = mutableMapOf()
            drawables[shaderProgram] = nodesByShader
        }
        var nodesByShaderAndTexture = nodesByShader[drawable.texture]
        if (nodesByShaderAndTexture == null) {
            nodesByShaderAndTexture = BatchBundle(
                    BatchBuffer(defaultVertexSize * 3),
                    BatchBuffer(defaultVertexSize * 4),
                    BatchBuffer(defaultVertexSize * 2))
            nodesByShader[drawable.texture] = nodesByShaderAndTexture
        }
        nodesByShaderAndTexture.apply {
            val positionBufferData = positionBuffer.add(drawable.positions())
            val texcoordBufferData = texcoordBuffer.add(drawable.texcoords())
            val colorBufferData = colorBuffer.add(drawable.colors())
            drawables[drawable] = BatchNode(positionBufferData, colorBufferData, texcoordBufferData)
        }
    }

    fun add(drawableGroup: DrawableGroup, shaderProgram: GLShaderProgram) {
        groups.add(drawableGroup)
        drawableGroup.nodes.forEach { this.add(it.drawable, shaderProgram) }
    }

    fun remove(drawable: Drawable) {
        val shaderProgram = shaders[drawable] ?: return
        val nodesByShaderAndTexture = drawables[shaderProgram]?.get(drawable.texture) ?: return
        val batchData = nodesByShaderAndTexture.drawables[drawable] ?: return
        shaders.remove(drawable)
        nodesByShaderAndTexture.positionBuffer.remove(batchData.positionBufferData)
        nodesByShaderAndTexture.colorBuffer.remove(batchData.colorBufferData)
        nodesByShaderAndTexture.texcoordBuffer.remove(batchData.texcoordBufferData)
        nodesByShaderAndTexture.drawables.remove(drawable)
    }

    fun remove(drawableGroup: DrawableGroup) {
        drawableGroup.nodes.forEach { remove(it.drawable) }
        groups.remove(drawableGroup)
    }

    fun draw(delta: Float, camera: Camera) {
        var currentShaderProgram: GLShaderProgram? = null
        var currentTexture: GLTexture? = null
        var currentTextureEnable = true

        drawables.keys.forEach shader@{ shaderProgram ->
            val nodesByTexture = drawables[shaderProgram] ?: return@shader
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

    fun destroy() {
        drawables.values.forEach { node ->
            node.values.forEach {
                it.positionBuffer.destroy()
                it.colorBuffer.destroy()
                it.texcoordBuffer.destroy()
            }
        }
        drawables.clear()
        shaders.clear()
    }
}