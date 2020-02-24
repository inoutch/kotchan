package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.material.Material
import io.github.inoutch.kotchan.extension.fastForEach

abstract class BatchBase<TObject>(val material: Material) : Disposer() {
    data class Bundle(val stride: Int, val batchBuffer: BatchBuffer)

    abstract val bundles: List<Bundle>

    private val objectBundles = mutableListOf<BatchObjectBufferBundle<TObject>>()

    abstract fun update(objectBundle: BatchObjectBufferBundle<TObject>)

    abstract fun size(obj: TObject): Int

    fun add(obj: TObject, drawOrder: Int = 0) {
        objectBundles.add(allocate(obj, size(obj), drawOrder))
    }

    fun render() {
        material.bind()

        var i = 0
        while (i < objectBundles.size) {
            update(objectBundles[i])
            i++
        }
        flushAll()
        graphic.drawTriangles(bundles.map { it.batchBuffer.vertexBuffer }, triangleCount())
    }

    fun sortByRenderOrder() {
        objectBundles.sortBy { it.index }

        // Replace by pointer
        var i = 0
        while (i < bundles.size) {
            bundles[i].batchBuffer.sort { adder ->
                objectBundles.fastForEach {
                    adder.invoke(it.pointers[i])
                }
            }
            i++
        }
    }

    private fun triangleCount(): Int {
        return bundles.first().let { it.batchBuffer.size / it.stride }
    }

    private fun allocate(obj: TObject, size: Int, index: Int): BatchObjectBufferBundle<TObject> {
        val data = bundles.map { it.batchBuffer.allocate(it.stride * size) }
        return BatchObjectBufferBundle(index, obj, data)
    }

    private fun flushAll() {
        bundles.fastForEach { it.batchBuffer.flush() }
    }
}
