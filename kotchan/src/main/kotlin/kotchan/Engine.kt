package kotchan

import application.AppConfig
import application.AppScene
import interop.graphic.GL
import interop.graphic.GLCamera
import interop.io.File
import kotchan.constant.ScreenType
import kotchan.controller.TouchEmitter
import kotchan.controller.TouchController
import kotchan.controller.TouchControllerEntity
import kotchan.texture.TextureManager
import kotchan.scene.Scene
import utility.type.Rect
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
    var viewport: Rect = Rect()
        private set

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
                viewport = Rect(Vector2(0.0f, 0.0f), windowSize)
            }
            ScreenType.FIX_WIDTH -> {
                this.screenSize = Vector2(screenSize.x, screenSize.x * windowRatio)
                viewport = Rect(Vector2(0.0f, 0.0f), windowSize)
            }
            ScreenType.FIX_HEIGHT -> {
                this.screenSize = Vector2(screenSize.y / windowRatio, screenSize.y)
                viewport = Rect(Vector2(0.0f, 0.0f), windowSize)
            }
            ScreenType.BORDER -> {
                this.screenSize = screenSize
                val screenRatio = screenSize.y / screenSize.x
                viewport = when {
                    windowRatio > screenRatio -> {
                        // top-bottom
                        val border = (windowSize.y - windowSize.x * screenRatio) / 2.0f
                        Rect(Vector2(0.0f, border), Vector2(windowSize.x, windowSize.y - border))
                    }
                    windowRatio < screenRatio -> {
                        // right-left
                        val border = (windowSize.x - windowSize.y / screenRatio) / 2.0f
                        Rect(Vector2(border, 0.0f), Vector2(windowSize.x - border, windowSize.y))
                    }
                    else -> Rect(Vector2(0.0f, 0.0f), windowSize)
                }
            }
        }
        currentScene = AppScene()
    }

    fun draw(delta: Float) {
        gl.viewPort(viewport.origin.x.toInt(), viewport.origin.y.toInt(), viewport.size.x.toInt(), viewport.size.y.toInt())
        currentScene?.draw(delta)
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        currentScene?.reshape(x, y, width, height)
    }

    fun createOrthographicCamera(): GLCamera {
        println(screenSize)
        return GLCamera.createOrthographic(0.0f, screenSize.x, 0.0f, screenSize.y, -1.0f, 1.0f)
    }
}