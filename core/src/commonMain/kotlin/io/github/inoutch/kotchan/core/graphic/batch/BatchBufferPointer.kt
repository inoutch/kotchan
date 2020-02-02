package io.github.inoutch.kotchan.core.graphic.batch

class BatchBufferPointer(var first: Int, var size: Int) {
    fun last() = first + size
}
