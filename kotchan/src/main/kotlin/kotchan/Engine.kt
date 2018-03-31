package kotchan

import application.AppDelegate
import interop.graphic.GL
import kotchan.view.Texture
import kotchan.view.View

//  Do not create Engine instance!
class Engine {
    companion object {
        private var engine: Engine? = null
        fun getInstance() = engine as Engine
    }

    val gl = GL()
    val fileManager = null
    val textureManager = null
    val shaderManager = null

    private var currentView: View? = null

    init {
        if (Engine.engine != null) {
            throw RuntimeException("Engine should be created only 1")
        }
        Engine.engine = this
    }

    fun init(x: Int, y: Int, width: Int, height: Int) {
        gl.viewPort(x, y, width, height)
        currentView = AppDelegate()
    }

    fun render(delta: Float) {
        currentView?.render(delta)
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        // renderer.scene?.reshape()
    }
}