package io.github.inoutch.kotchan.extension

operator fun <U, T : Any> Map<U, T>.get(list: List<U>): List<T> {
    return list.mapNotNull { this[it] }
}