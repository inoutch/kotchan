package kotchan

import application.AppScene
import interop.graphic.GL
import interop.graphic.GLTexture
import interop.io.File
import kotchan.controller.TouchEmitter
import kotchan.controller.TouchController
import kotchan.controller.TouchControllerEntity
import kotchan.texture.TextureManager
import kotchan.scene.Scene
import utility.type.Vector2

// Do not create Engine instance!
class Engine {
    companion object {
        private var engine: Engine? = null
        fun getInstance() = engine as Engine
    }

    private var currentScene: Scene? = null
    private val touchControllerEntity = TouchControllerEntity()

    val gl = GL()
    val file = File()

    val textureManager = TextureManager(gl)

    // actual window size, but you should not use it
    var windowSize: Vector2 = Vector2(0.0f, 0.0f)
    var screenSize: Vector2 = Vector2(0.0f, 0.0f)

    val touchEmitter: TouchEmitter = touchControllerEntity
    val touchController: TouchController = touchControllerEntity

    init {
        if (Engine.engine != null) {
            throw Error("Engine should be created only 1")
        }
        Engine.engine = this
    }

    fun init(windowSize: Vector2, screenSize: Vector2) {
        this.windowSize = windowSize
        this.screenSize = screenSize
        gl.viewPort(0, 0, windowSize.x.toInt(), windowSize.y.toInt())
        currentScene = AppScene()
    }

    fun render(delta: Float) {
        touchControllerEntity.begin()
        currentScene?.render(delta)
        touchControllerEntity.end()
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        // renderer.scene?.reshape()
    }

}