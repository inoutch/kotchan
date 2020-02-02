package io.github.inoutch.kotchan.core.graphic.compatible.context

import io.github.inoutch.kotchan.core.Disposable
import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferBundle
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.math.Vector2I

interface Context : Disposable {
    fun begin()
    fun end()
    fun resize(windowSize: Vector2I)
    fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer
//    fun copyToBuffer()
    fun drawTriangles(batchBufferBundle: BatchBufferBundle)
//    fun createShader(shaderSource: ShaderSource): Shader
//    fun createGraphicsPipeline()
//    fun bindGraphicsPipeline()
//    fun setViewport(viewport: RectI)
//    fun setScissor(scissor: RectI)
//    fun clearColor()
//    fun clearDepth()
//    fun setUniform1f()
//    fun setUniform1i()
//    fun setUniform3f()
//    fun setUniform4f()
//    fun setUniformMatrix4f()
    fun loadTexture(image: Image): Texture
}
