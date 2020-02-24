package io.github.inoutch.kotchan.core.graphic.batch

class BatchObjectBufferBundle<T>(
    val index: Int,
    val obj: T,
    val buffers: List<BatchObjectBuffer>
)
