package kotchan

import application.AppConfig
import application.AppScene
import interop.graphic.GL
import interop.io.File
import kotchan.constant.ScreenType
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

        val windowRatio = windowSize.y / windowSize.x
        when (AppConfig.SCREEN_TYPE) {
            ScreenType.EXTEND -> {
                this.screenSize = screenSize
                gl.viewPort(0, 0, windowSize.x.toInt(), windowSize.y.toInt())
            }
            ScreenType.FIX_WIDTH -> {
                this.screenSize = Vector2(screenSize.x, screenSize.x * windowRatio)
                gl.viewPort(0, 0, windowSize.x.toInt(), windowSize.y.toInt())
            }
            ScreenType.FIX_HEIGHT -> {
                this.screenSize = Vector2(screenSize.y / windowRatio, screenSize.y)
                gl.viewPort(0, 0, windowSize.x.toInt(), windowSize.y.toInt())
            }
            ScreenType.BORDER -> {
                val screenRatio = screenSize.y / screenSize.x
                this.screenSize = screenSize
                if (windowRatio > screenRatio) {
                    // top-bottom
                    val border = (windowSize.y - windowSize.x * screenRatio) / 2.0f
                    gl.viewPort(0, border.toInt(), windowSize.x.toInt(), windowSize.y.toInt())
                } else if (windowRatio < screenRatio) else {
                    // right-left
                    val border = (windowSize.x - windowSize.y / screenRatio) / 2.0f
                    gl.viewPort(border.toInt(), 0, windowSize.x.toInt(), windowSize.y.toInt())
                }
            }
        }
        currentScene = AppScene()
    }

    fun draw(delta: Float) {
        currentScene?.draw(delta)
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        currentScene?.reshape(x, y, width, height)
    }
}