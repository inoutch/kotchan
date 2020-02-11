package io.github.inoutch.kotchan.core

/*
    Disposer disposes in reverse order of registered targets
 */
open class Disposer : Disposable {
    private val disposableCallbackMap = mutableMapOf<Any, () -> Unit>()

    private val disposableCallbacks = mutableListOf<() -> Unit>()

    private var isDisposedEntity = false

    private var parents = mutableListOf<Disposer>()

    override fun isDisposed(): Boolean = isDisposedEntity

    fun <T : Disposable> add(disposable: T) {
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
        check(!disposableCallbackMap.containsKey(id)) { "Already added the same id" }
        disposableCallbackMap[id] = callback
        disposableCallbacks.add(callback)
    }

    fun <T : Disposer> add(disposer: T): T {
        check(!parents.contains(disposer)) { "Already added the parent disposer" }
        disposer.register(this)
        add(disposer as Disposable)
        return disposer
    }

    fun <T : Disposer> add(disposers: List<T>): List<T> {
        disposers.forEach { add(it) }
        return disposers
    }

    fun remove(disposable: Disposable) {
        val disposableCallback = disposableCallbackMap[disposable]
        disposableCallbacks.remove(disposableCallback)
        disposableCallbackMap.remove(disposable)
    }

    fun remove(disposer: Disposer) {
        disposer.unregister(this)
        remove(disposer as Disposable)
    }

    fun remove(id: Any): (() -> Unit)? {
        val callback = disposableCallbackMap[id]
        disposableCallbacks.remove(callback)
        disposableCallbackMap.remove(id)
        return callback
    }

    fun removeAll() {
        disposableCallbackMap
                .keys
                .filterIsInstance<Disposer>()
                .forEach { it.unregister(this) }
        disposableCallbackMap.clear()
        disposableCallbacks.clear()
    }

    fun dispose(disposable: Disposable) {
        remove(disposable)
        disposable.dispose()
    }

    fun dispose(id: Any) {
        remove(id)?.invoke()
    }

    override fun dispose() {
        if (isDisposedEntity) {
            return
        }

        isDisposedEntity = true
        // Dispose children
        disposableCallbackMap.clear()
        disposableCallbacks.reversed().forEach { it.invoke() }

        // Notify itself was disposed to the parents
        parents.toList().forEach { it.remove(this) }
        parents.clear()
    }

    private fun register(parentDisposer: Disposer) {
        parents.add(parentDisposer)
    }

    private fun unregister(parentDisposer: Disposer) {
        parents.remove(parentDisposer)
    }
}
