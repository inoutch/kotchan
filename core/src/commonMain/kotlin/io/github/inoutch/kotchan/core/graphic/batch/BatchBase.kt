package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.material.Material

abstract class BatchBase<TObject>(val material: Material): Disposer() {
    abstract val bufferBundle: BatchBufferBundle<TObject>

    private val objectBundles = mutableListOf<BatchObjectBufferBundle<TObject>>()

    abstract fun update(objectBundle: BatchObjectBufferBundle<TObject>)

    abstract fun size(obj: TObject): Int

    fun add(obj: TObject, drawOrder: Int = 0) {
        objectBundles.add(bufferBundle.allocate(obj, size(obj), drawOrder))
    }

    fun render() {
        material.bind()

        var i = 0
        while (i < objectBundles.size) {
            update(objectBundles[i])
            i++
        }
        bufferBundle.flushAll()
        graphic.drawTriangles(bufferBundle)
    }

    fun sortByRenderOrder() {
        objectBundles.sortBy { it.index }
        val buffers = bufferBundle.buffers
        var i = 0
        while (i < buffers.size) {
            val buffer = buffers[i]
            buffer.sort {
                var j = 0
                while (j < objectBundles.size) {
                    it.invoke(objectBundles[j].buffers[i].batchBufferPointer)
                    j++
                }
            }
            i++
        }
    }
}
