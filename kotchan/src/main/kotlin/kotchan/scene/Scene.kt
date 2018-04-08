package kotchan.scene

import kotchan.Engine
import kotchan.event.TouchInterface

abstract class Scene {
    protected val gl = Engine.getInstance().gl
    protected val file = Engine.getInstance().file
    protected val textureManager = Engine.getInstance().textureManager
    protected val touchInterface = Engine.getInstance().touchInterface
    protected val windowSize = Engine.getInstance().windowSize
    protected val screenSize = Engine.getInstance().screenSize

    abstract fun render(delta: Float)
    abstract fun pause()
    abstract fun resume()
}