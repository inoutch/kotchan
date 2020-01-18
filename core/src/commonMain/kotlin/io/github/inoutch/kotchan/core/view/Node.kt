package io.github.inoutch.kotchan.core.view

import io.github.inoutch.kotchan.core.Disposable
import io.github.inoutch.kotchan.core.Disposer

abstract class Node : Disposable {
    protected val disposer = Disposer()

    private val children = mutableListOf<Node>()

    fun addChild(child: Node) {
        children.add(child)
        disposer.add(child.disposer)
    }

    fun removeChild(child: Node) {
        children.remove(child)
        disposer.remove(child.disposer)
    }

    abstract suspend fun render(delta: Float)

    override fun isDisposed(): Boolean {
        return disposer.isDisposed()
    }

    override fun dispose() {
        disposer.dispose()
    }
}
