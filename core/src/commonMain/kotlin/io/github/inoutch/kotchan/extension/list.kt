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

fun <T> List<T>.toPairs(): List<Pair<T, T>> {
    val pairs = mutableListOf<Pair<T, T>>()
    for (x in this.indices) {
        for (y in x + 1 until this.size) {
            pairs.add(Pair(this[x], this[y]))
        }
    }
    return pairs
}
