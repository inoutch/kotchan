package kotchan.view

import kotchan.Engine

abstract class Scene {
    protected val gl = Engine.getInstance().gl
    protected val file = Engine.getInstance().file
    protected val textureManager = Engine.getInstance().textureManager
    protected val touchController = Engine.getInstance().touchController
    protected val animator = Engine.getInstance().animator
    protected val windowSize = Engine.getInstance().windowSize
    protected val screenSize = Engine.getInstance().screenSize

    abstract fun draw(delta: Float)
    abstract fun reshape(x: Int, y: Int, width: Int, height: Int)
    abstract fun pause()
    abstract fun resume()
    abstract fun destroyed()
}