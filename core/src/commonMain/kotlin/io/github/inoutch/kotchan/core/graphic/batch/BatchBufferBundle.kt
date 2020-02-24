package io.github.inoutch.kotchan.core.graphic.batch

class BatchBufferBundle<T>(infos: List<BufferInfo> = createDefaultBufferInfos()) {
    val bufferInfos = infos.map { BufferInfoBundle(it, BatchBuffer()) }

    val buffers: List<BatchBuffer>
        get() = bufferInfos.map { it.buffer }

    fun getBuffer(index: Int): BatchBuffer {
        return bufferInfos[index].buffer
    }

    data class BufferInfo(val size: Int)

    data class BufferInfoBundle(val info: BufferInfo, val buffer: BatchBuffer)

    companion object {
        const val POSITION_SIZE = 3
        const val COLOR_SIZE = 4
        const val TEXCOORD_SIZE = 2

        fun createDefaultBufferInfos(): List<BufferInfo> {
            return listOf(
                    BufferInfo(POSITION_SIZE),
                    BufferInfo(COLOR_SIZE),
                    BufferInfo(TEXCOORD_SIZE)
            )
        }
    }

    val size: Int
        get() = bufferInfos.first().let { it.buffer.size / it.info.size }

    fun allocate(obj: T, size: Int, index: Int): BatchObjectBufferBundle<T> {
        val data = bufferInfos.map { BatchObjectBuffer(it.buffer, it.buffer.allocate(it.info.size * size)) }
        return BatchObjectBufferBundle(index, obj, data)
    }

    fun flushAll() {
        bufferInfos.forEach { it.buffer.flush() }
    }
}
