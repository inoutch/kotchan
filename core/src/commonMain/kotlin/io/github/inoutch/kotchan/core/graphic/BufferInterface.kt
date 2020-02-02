package io.github.inoutch.kotchan.core.graphic

interface BufferInterface<T> {
    fun range(first: Int, last: Int)

    fun copy(offset: Int, value: T)

    fun resize(size: Int)
}
