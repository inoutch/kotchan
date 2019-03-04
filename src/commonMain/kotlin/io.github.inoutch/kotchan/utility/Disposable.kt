package io.github.inoutch.kotchan.utility

interface Disposable {
    fun dispose()
}

class DisposeManager : Disposable {
    private val disposables = mutableListOf<Disposable>()

    fun <T : Disposable> add(disposable: T): T {
        disposables.add(disposable)
        return disposable
    }

    fun dispose(disposable: Disposable) {
        disposables.remove(disposable)
        disposable.dispose()
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}

fun <T> disposeScoped(scope: DisposeManager.() -> T): T {
    val disposeManager = DisposeManager()

    try {
        val ret = scope(disposeManager)
        disposeManager.dispose()
        return ret
    } catch (e: Error) {

        disposeManager.dispose()
        throw e
    }
}
