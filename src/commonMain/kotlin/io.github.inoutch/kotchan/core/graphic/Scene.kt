package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.Disposer
import io.github.inoutch.kotchan.utility.DisposerAppender

abstract class Scene : Disposable {

    protected val file = core.file

    protected val textureCacheManager = core.textureCacheManager

    protected val touchController = core.touchController

    protected val animator = core.animator

    protected val windowSize = core.windowSize

    protected val screenSize = core.screenSize

    protected val disposer: DisposerAppender
        get() = privateDisposer

    private val privateDisposer = Disposer()

    abstract fun draw(delta: Float)

    abstract fun reshape(x: Int, y: Int, width: Int, height: Int)

    abstract fun pause()

    abstract fun resume()

    override fun dispose() {
        privateDisposer.dispose()
    }
}
