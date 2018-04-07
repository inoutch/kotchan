package kotchan

import application.AppScene
import interop.graphic.GL
import interop.io.File
import kotchan.event.TouchInterface
import kotchan.event.TouchManager
import kotchan.texture.TextureManager
import kotchan.scene.Scene
import utility.type.Size

// Do not create Engine instance!
class Engine {
    companion object {
        private var engine: Engine? = null
        fun getInstance() = engine as Engine
    }

    private var currentScene: Scene? = null
    private val touchManager = TouchManager()

    val gl = GL()
    val file = File()

    val textureManager = TextureManager(gl)
    val touchInterface = TouchInterface(touchManager)

    var windowSize: Size = Size(0.0f, 0.0f)
    var screenSize: Size = Size(0.0f, 0.0f)


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
        currentScene = AppScene()
    }

    fun render(delta: Float) {
        gl.debug()
        currentScene?.render(delta)
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        // renderer.scene?.reshape()
    }


}