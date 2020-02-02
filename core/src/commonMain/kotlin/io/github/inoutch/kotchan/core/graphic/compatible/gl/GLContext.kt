package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferBundle
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.math.Vector2I

class GLContext : Context {
    override fun begin() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun end() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun resize(windowSize: Vector2I) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer {
        return GLVertexBuffer(vertices, bufferStorageMode)
    }

    override fun drawTriangles(batchBufferBundle: BatchBufferBundle) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTexture(image: Image): Texture {
        return GLTexture(image)
    }

    override fun isDisposed(): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
