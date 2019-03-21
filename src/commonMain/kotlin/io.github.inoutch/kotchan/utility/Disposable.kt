package io.github.inoutch.kotchan.utility

interface Disposable {
    fun dispose()
}

interface DisposerAppender {
    fun <T : Disposable> add(disposable: T): T
}

class Disposer : Disposable, DisposerAppender {
    private val disposables = mutableListOf<Disposable>()

    override fun <T : Disposable> add(disposable: T): T {
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

fun <T> disposeScoped(scope: Disposer.() -> T): T {
    val disposeManager = Disposer()

    try {
        val ret = scope(disposeManager)
        disposeManager.dispose()
        return ret
    } catch (e: Error) {

        disposeManager.dispose()
        throw e
    }
}
