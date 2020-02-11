package io.github.inoutch.kotchan.core.graphic.compatible.context

import io.github.inoutch.kotchan.core.Disposable
import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferBundle
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Shader
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderSource
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTexture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1I
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform2F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform3F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform4F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector4F

interface Context : Disposable {
    fun begin()
    fun end()
    fun resize(windowSize: Vector2I)
    fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer
    fun drawTriangles(batchBufferBundle: BatchBufferBundle)
    fun createShader(shaderSource: ShaderSource): Shader
    fun createGraphicsPipeline(shaderProgram: ShaderProgram, config: GraphicsPipelineConfig): GraphicsPipeline
    fun bindGraphicsPipeline(graphicsPipeline: GraphicsPipeline)
    fun setViewport(viewport: RectI)
    fun setScissor(scissor: RectI)
    fun clearColor(color: Vector4F)
    fun clearDepth(depth: Float)
    fun loadTexture(image: Image): Texture
    fun emptyTexture(): Texture
    fun createUniform1I(binding: Int, uniformName: String): Uniform1I
    fun createUniform1F(binding: Int, uniformName: String): Uniform1F
    fun createUniform2F(binding: Int, uniformName: String): Uniform2F
    fun createUniform3F(binding: Int, uniformName: String): Uniform3F
    fun createUniform4F(binding: Int, uniformName: String): Uniform4F
    fun createUniformMatrix4F(binding: Int, uniformName: String): UniformMatrix4F
    fun createUniformTexture(binding: Int, uniformName: String): UniformTexture
}
