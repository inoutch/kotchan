package io.github.inoutch.kotchan.test.utillity.mock

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
import io.github.inoutch.kotchan.test.utillity.logger.TestLogger

class MockContext(val testLogger: TestLogger) : Context {
    override fun begin() {
        testLogger.log(this, "begin")
    }

    override fun end() {
        testLogger.log(this, "end")
    }

    override fun resize(windowSize: Vector2I) {
        testLogger.log(this, "end")
    }

    override fun createVertexBuffer(vertices: IntArray, bufferStorageMode: BufferStorageMode): VertexBuffer {
        testLogger.log(this, "createVertexBuffer:int: ${vertices.size}, $bufferStorageMode, ${vertices.toList()}")
        return MockVertexBuffer(testLogger, vertices.size, bufferStorageMode)
    }

    override fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer {
        testLogger.log(this, "createVertexBuffer:float: ${vertices.size}, $bufferStorageMode, ${vertices.toList()}")
        return MockVertexBuffer(testLogger, vertices.size, bufferStorageMode)
    }

    override fun drawTriangles(buffers: List<VertexBuffer>, triangleCount: Int) {
        testLogger.log(this, "drawTriangles: ${buffers.size}, $triangleCount")
    }

    override fun createShader(shaderSource: ShaderSource, attributes: List<Attribute>): Shader {
        testLogger.log(this, "createShader: ${attributes.map { it.locationName }}")
        throw UnsupportedOperationException()
    }

    override fun createGraphicsPipeline(shaderProgram: ShaderProgram, config: GraphicsPipelineConfig): GraphicsPipeline {
        throw UnsupportedOperationException()
    }

    override fun bindGraphicsPipeline(graphicsPipeline: GraphicsPipeline) {
        testLogger.log(this, "bindGraphicsPipeline: ${graphicsPipeline.hashCode()}")
    }

    override fun setViewport(viewport: RectI) {
        testLogger.log(this, "setViewport: ${viewport.origin.x}, ${viewport.origin.y}, ${viewport.size.x}, ${viewport.size.y}")
    }

    override fun setScissor(scissor: RectI) {
        testLogger.log(this, "setScissor: ${scissor.origin.x}, ${scissor.origin.y}, ${scissor.size.x}, ${scissor.size.y}")
    }

    override fun clearColor(color: Vector4F) {
        testLogger.log(this, "clearColor: ${color.x}, ${color.y}, ${color.z}, ${color.w}")
    }

    override fun clearDepth(depth: Float) {
        testLogger.log(this, "clearDepth: $depth")
    }

    override fun loadTexture(image: Image, config: Texture.Config): Texture {
        testLogger.log(this, "loadTexture: ${image.hashCode()}, ${config.hashCode()}")
        throw UnsupportedOperationException()
    }

    override fun emptyTexture(): Texture {
        throw UnsupportedOperationException()
    }

    override fun createUniform1I(binding: Int, uniformName: String): Uniform1I {
        throw UnsupportedOperationException()
    }

    override fun createUniform1F(binding: Int, uniformName: String): Uniform1F {
        throw UnsupportedOperationException()
    }

    override fun createUniform2F(binding: Int, uniformName: String): Uniform2F {
        throw UnsupportedOperationException()
    }

    override fun createUniform3F(binding: Int, uniformName: String): Uniform3F {
        throw UnsupportedOperationException()
    }

    override fun createUniform4F(binding: Int, uniformName: String): Uniform4F {
        throw UnsupportedOperationException()
    }

    override fun createUniformMatrix4F(binding: Int, uniformName: String): UniformMatrix4F {
        throw UnsupportedOperationException()
    }

    override fun createUniformTexture(binding: Int, uniformName: String): UniformTexture {
        throw UnsupportedOperationException()
    }

    override fun createUniformTextureArray(binding: Int, uniformName: String, size: Int): UniformTextureArray {
        throw UnsupportedOperationException()
    }

    override fun isDisposed(): Boolean {
        return false
    }

    override fun dispose() {
        testLogger.log(this, "dispose")
    }
}
