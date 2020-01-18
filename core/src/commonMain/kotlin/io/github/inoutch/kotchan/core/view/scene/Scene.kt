package io.github.inoutch.kotchan.core.view.scene

import io.github.inoutch.kotchan.core.view.Node

abstract class Scene(context: SceneContext) : Node() {
    protected val sceneManager = context.sceneManager

    abstract suspend fun init()

    abstract suspend fun update(delta: Float)
}
