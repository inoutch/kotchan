package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferBundle
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Shader
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderSource
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.Attribute
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1I
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform2F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform3F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform4F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTexture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTextureArray
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotlin.gl.api.GL_ARRAY_BUFFER
import io.github.inoutch.kotlin.gl.api.GL_COLOR_BUFFER_BIT
import io.github.inoutch.kotlin.gl.api.GL_DEPTH_BUFFER_BIT
import io.github.inoutch.kotlin.gl.api.GL_FLOAT
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_2D
import io.github.inoutch.kotlin.gl.api.GL_TRIANGLES
import io.github.inoutch.kotlin.gl.api.gl
import io.github.inoutch.kotlin.gl.constant.FLOAT_BYTE_SIZE

class GLContext : Context {
    private val emptyTexture = loadTexture(Image(byteArrayOf(-1, -1, -1, -1), Vector2I(1, 1)))

    private var currentGraphicsPipeline: GraphicsPipeline? = null

    init {
        gl.enableVertexAttribArray(0)
    }

    override fun begin() {}

    override fun end() {
        gl.bindTexture(GL_TEXTURE_2D, 0)
        gl.useProgram(0)
    }

    override fun resize(windowSize: Vector2I) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer {
        return GLVertexBuffer(vertices, bufferStorageMode)
    }

    override fun drawTriangles(batchBufferBundle: BatchBufferBundle<*>) {
        val graphicsPipeline = currentGraphicsPipeline ?: return
        val attributes = graphicsPipeline.shaderProgram.shader.attributes
        var i = 0
        while (i < batchBufferBundle.bufferInfos.size) {
            val buffer = batchBufferBundle.getBuffer(i).vertexBuffer as GLVertexBuffer
            gl.bindBuffer(GL_ARRAY_BUFFER, buffer.id)
            vertexPointer(attributes[i])
            i++
        }

        gl.drawArrays(GL_TRIANGLES, 0, batchBufferBundle.size)
    }

    override fun createShader(shaderSource: ShaderSource, attributes: List<Attribute>): Shader {
        return GLShader(shaderSource.glslVertSource, shaderSource.glslFragSource, attributes)
    }

    override fun createGraphicsPipeline(
            shaderProgram: ShaderProgram,
            config: GraphicsPipelineConfig
    ): GraphicsPipeline {
        val uniforms = shaderProgram.descriptorSets.filterIsInstance<GLUniform>()
        val uniformTextures = shaderProgram.descriptorSets.filterIsInstance<GLUniformTexture>()
        return GLGraphicsPipeline(
                shaderProgram,
                config,
                shaderProgram.shader as GLShader,
                uniforms,
                uniformTextures
        )
    }

    override fun bindGraphicsPipeline(graphicsPipeline: GraphicsPipeline) {
        currentGraphicsPipeline = graphicsPipeline
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

    override fun loadTexture(image: Image, config: Texture.Config): Texture {
        return GLTexture(image, config)
    }

    override fun emptyTexture(): Texture {
        return emptyTexture
    }

    override fun createUniform1I(binding: Int, uniformName: String): Uniform1I {
        return GLUniform1I(binding, uniformName)
    }

    override fun createUniform1F(binding: Int, uniformName: String): Uniform1F {
        return GLUniform1F(binding, uniformName)
    }

    override fun createUniform2F(binding: Int, uniformName: String): Uniform2F {
        return GLUniform2F(binding, uniformName)
    }

    override fun createUniform3F(binding: Int, uniformName: String): Uniform3F {
        return GLUniform3F(binding, uniformName)
    }

    override fun createUniform4F(binding: Int, uniformName: String): Uniform4F {
        return GLUniform4F(binding, uniformName)
    }

    override fun createUniformMatrix4F(binding: Int, uniformName: String): UniformMatrix4F {
        return GLUniformMatrix4F(binding, uniformName)
    }

    override fun createUniformTexture(binding: Int, uniformName: String): UniformTexture {
        return GLUniformTexture(binding, uniformName)
    }

    override fun createUniformTextureArray(binding: Int, uniformName: String): UniformTextureArray {
        return GLUniformTextureArray(binding, uniformName)
    }

    override fun isDisposed(): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun vertexPointer(attributeLocation: GLAttribLocation, dimension: Int, stride: Int) {
        gl.vertexAttribPointer(attributeLocation.value, dimension, GL_FLOAT, false, stride * FLOAT_BYTE_SIZE)
        gl.enableVertexAttribArray(attributeLocation.value)
    }

    private fun vertexPointer(attribute: Attribute) {
        println("${attribute.location}, ${attribute.type.size}, ${attribute.type.toGLType()}, ${false}, ${attribute.stride * attribute.type.size}")
        gl.vertexAttribPointer(
                attribute.location,
                attribute.stride,
                attribute.type.toGLType(),
                false,
                attribute.stride * attribute.type.size
        )
        gl.enableVertexAttribArray(attribute.location)
    }
}
