package io.github.inoutch.kotchan.extension

inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    var i = 0
    while (i < size) {
        action(this[i])
        i++
    }
}

inline fun <T> List<T>.fastForEachIndexed(action: (Int, T) -> Unit) {
    var i = 0
    while (i < size) {
        action(i, this[i])
        i++
    }
}
