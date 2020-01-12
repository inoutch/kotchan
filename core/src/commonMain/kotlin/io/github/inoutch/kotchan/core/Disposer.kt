package io.github.inoutch.kotchan.core

class Disposer : Disposable {
    private val disposableCallbackMap = mutableMapOf<Any, () -> Unit>()
    private val disposableCallbacks = mutableListOf<() -> Unit>()

    fun add(disposable: Disposable) {
        val callback = {
            disposableCallbackMap[disposable]
            disposable.dispose()
        }
        disposableCallbackMap[disposable] = callback
        disposableCallbacks.add(callback)
    }

    fun add(callback: () -> Unit) {
        disposableCallbacks.add(callback)
    }

    fun add(id: Any, callback: () -> Unit) {
        disposableCallbackMap[id] = callback
        disposableCallbacks.add(callback)
    }

    fun remove(disposable: Disposable) {
        val disposableCallback = disposableCallbackMap[disposable]
        disposableCallbacks.remove(disposableCallback)
        disposableCallbackMap.remove(disposable)
    }

    fun remove(id: Any): (() -> Unit)? {
        val callback = disposableCallbackMap[id]
        disposableCallbacks.remove(callback)
        disposableCallbackMap.remove(id)
        return callback
    }

    fun dispose(disposable: Disposable) {
        remove(disposable)
        disposable.dispose()
    }

    fun dispose(id: Any) {
        remove(id)?.invoke()
    }

    override fun dispose() {
        disposableCallbackMap.clear()
        disposableCallbacks.reversed().forEach { it.invoke() }
    }
}