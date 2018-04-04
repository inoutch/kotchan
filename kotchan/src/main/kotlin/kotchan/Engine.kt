package kotchan

import application.AppDelegate
import interop.graphic.GL
import interop.io.File
import kotchan.view.View
import utility.type.Size

// Do not create Engine instance!
class Engine {
    companion object {
        private var engine: Engine? = null
        fun getInstance() = engine as Engine
    }

    val gl = GL()
    val file = File()

    val textureManager = TextureManager(gl)
    var windowSize: Size = Size(0.0f, 0.0f)
    var screenSize: Size = Size(0.0f, 0.0f)

    private var currentView: View? = null

    init {
        if (Engine.engine != null) {
            throw Error("Engine should be created only 1")
        }
        Engine.engine = this
    }

    fun init(windowSize: Size, screenSize: Size) {
        this.windowSize = windowSize
        this.screenSize = screenSize
        gl.viewPort(0, 0, windowSize.width.toInt(), windowSize.height.toInt())
        currentView = AppDelegate()
    }

    fun render(delta: Float) {
        gl.debug()
        currentView?.render(delta)
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        // renderer.scene?.reshape()
    }
}