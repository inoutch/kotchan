package io.github.inoutch.kotchan.core

interface Disposable {

    fun isDisposed(): Boolean

    fun dispose()
}

fun <T: Disposable>List<T>.dispose() {
    forEach { it.dispose() }
}
