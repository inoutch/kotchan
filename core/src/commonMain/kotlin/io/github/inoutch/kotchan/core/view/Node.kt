package io.github.inoutch.kotchan.core.view

import io.github.inoutch.kotchan.core.Disposable
import io.github.inoutch.kotchan.core.Drawable
import io.github.inoutch.kotchan.core.Updatable

abstract class Node : Updatable, Disposable, Drawable {
    private val children = mutableListOf<Node>()

    fun addChild(child: Node) {
        children.add(child)
    }


}
