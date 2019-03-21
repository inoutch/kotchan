package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.Disposer
import io.github.inoutch.kotchan.utility.DisposerAppender

abstract class Scene : Disposable {

    protected val file = KotchanCore.instance.file

    protected val textureCacheManager = KotchanCore.instance.textureCacheManager

    protected val touchController = KotchanCore.instance.touchController

    protected val animator = KotchanCore.instance.animator

    protected val windowSize = KotchanCore.instance.windowSize

    protected val screenSize = KotchanCore.instance.screenSize

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
