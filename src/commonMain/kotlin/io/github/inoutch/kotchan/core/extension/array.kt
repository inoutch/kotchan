package io.github.inoutch.kotchan.core.extension

inline fun <T> Array<out T>.fastForEach(action: (T) -> Unit) {
    var i = 0
    while (i < size) {
        action(this[i])
        i++
    }
}

inline fun <T> Array<out T>.fastForEachIndexed(action: (Int, T) -> Unit) {
    var i = 0
    while (i < size) {
        action(i, this[i])
        i++
    }
}
