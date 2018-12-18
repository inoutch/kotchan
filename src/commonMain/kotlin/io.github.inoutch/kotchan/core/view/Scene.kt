package io.github.inoutch.kotchan.core.view

import io.github.inoutch.kotchan.core.KotchanCore

abstract class Scene {

    protected val gl = KotchanCore.instance.gl

    protected val file = KotchanCore.instance.file

    protected val textureManager = KotchanCore.instance.textureManager

    protected val touchController = KotchanCore.instance.touchController

    protected val animator = KotchanCore.instance.animator

    protected val windowSize = KotchanCore.instance.windowSize

    protected val screenSize = KotchanCore.instance.screenSize

    abstract fun draw(delta: Float)

    abstract fun reshape(x: Int, y: Int, width: Int, height: Int)

    abstract fun pause()

    abstract fun resume()

    abstract fun destroyed()
}