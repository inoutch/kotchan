package io.github.inoutch.kotchan.core.graphic

import kotlin.math.max
import kotlin.math.min

class ChangeRange(size: Int) {
    var size: Int = size
        private set

    var change: IntRange? = IntRange(0, size)
        private set

    fun resize(size: Int) {
        this.size = size
        this.change = IntRange(0, size)
    }

    fun resizeAndUpdate(size: Int, first: Int) {
        this.size = size
        update(first, size)
    }

    fun update(first: Int, last: Int) {
        val change = this.change
        this.change = if (change == null) {
            IntRange(first, last)
        } else {
            IntRange(min(first, change.first), max(last, change.last))
        }
    }

    fun updateAll() {
        update(0, size)
    }

    fun reset() {
        change = null
    }
}
