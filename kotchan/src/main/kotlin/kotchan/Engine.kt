package kotchan

import application.AppDelegate
import interop.graphic.GL

//  Do not create Engine instance!
class Engine {
    companion object {
        private var engine: Engine? = null
        fun getInstance() = engine as Engine
    }

    private val renderer: ViewController

    val gl = GL()

    init {
        if (Engine.engine != null) {
            throw RuntimeException("Engine should be created only 1")
        }
        Engine.engine = this
        renderer = ViewController()
        renderer.runRender(AppDelegate())
    }

    fun render(delta: Float) {
        renderer.render(delta)
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        // renderer.scene?.reshape()
    }
}