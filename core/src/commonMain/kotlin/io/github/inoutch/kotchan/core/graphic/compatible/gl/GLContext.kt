package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferBundle
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Shader
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderSource
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotlin.gl.api.GL_COLOR_BUFFER_BIT
import io.github.inoutch.kotlin.gl.api.GL_DEPTH_BUFFER_BIT
import io.github.inoutch.kotlin.gl.api.gl

class GLContext : Context {
    override fun begin() {}

    override fun end() {}

    override fun resize(windowSize: Vector2I) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer {
        return GLVertexBuffer(vertices, bufferStorageMode)
    }

    override fun drawTriangles(batchBufferBundle: BatchBufferBundle) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createShader(shaderSource: ShaderSource): Shader {
        return GLShader(shaderSource.glslVertSource, shaderSource.glslFragSource)
    }

    override fun loadTexture(image: Image): Texture {
        return GLTexture(image)
    }

    override fun setViewport(viewport: RectI) {
        gl.viewport(viewport.origin.x, viewport.origin.y, viewport.size.x, viewport.size.y)
    }

    override fun setScissor(scissor: RectI) {
        gl.scissor(scissor.origin.x, scissor.origin.y, scissor.size.x, scissor.size.y)
    }

    override fun clearColor(color: Vector4F) {
        gl.clearColor(color.x, color.y, color.z, color.w)
        gl.clear(GL_COLOR_BUFFER_BIT)
    }

    override fun clearDepth(depth: Float) {
        gl.clearDepthf(depth)
        gl.clear(GL_DEPTH_BUFFER_BIT)
    }

    override fun isDisposed(): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
