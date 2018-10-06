package kotchan

import application.AppConfig
import application.AppScene
import interop.graphic.*
import kotchan.view.camera.Camera
import interop.io.File
import interop.singleton.Singleton
import interop.time.Time
import kotchan.view.animator.Animator
import kotchan.view.camera.Camera2D
import kotchan.view.camera.Camera3D
import kotchan.constant.ScreenType
import kotchan.controller.event.listener.EventController
import kotchan.controller.event.listener.TimerEventController
import kotchan.controller.touch.TouchEmitter
import kotchan.controller.touch.TouchController
import kotchan.controller.touch.TouchControllerEntity
import kotchan.view.texture.TextureManager
import kotchan.view.Scene
import kotchan.view.drawable.Square
import kotchan.view.shader.NoColorsShaderProgram
import utility.type.Matrix4
import utility.type.Rect
import utility.type.Vector2
import utility.type.Vector3

// Do not create Engine instance!
class Engine {
    companion object {
        fun getInstance() = Singleton.getInstance().get("engine") as Engine
    }

    private var currentScene: Scene? = null
    private var sceneFactory: (() -> Scene)? = null
    private val touchControllerEntity = TouchControllerEntity()
    private var beforeMillis: Long = 0

    private var frameBuffer: GLFrameBuffer? = null
    private var virtualView: Square? = null
    private var virtualViewShaderProgram: NoColorsShaderProgram? = null
    private var virtualCamera: Camera? = null

    val gl = GL()
    val file = File()

    val textureManager = TextureManager(gl)

    var windowSize: Vector2 = Vector2(0.0f, 0.0f)
    var screenSize: Vector2 = Vector2(0.0f, 0.0f)
    var viewport: Rect = Rect()
        private set

    val touchEmitter: TouchEmitter = touchControllerEntity
    val touchController: TouchController = touchControllerEntity
    val eventController: EventController = EventController()
    val timerEventController = TimerEventController()
    val animator = Animator()

    init {
        Singleton.getInstance().add("engine", this)
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

        frameBuffer = gl.createFrameBuffer().also {
            gl.bindFrameBuffer(it)

            val depthBuffer = gl.createRenderBuffer()

            gl.bindRenderBuffer(depthBuffer)
            gl.renderbufferStorage(GLInternalFormat.DEPTH16, this.screenSize.x.toInt(), this.screenSize.y.toInt())
            gl.attachRenderBuffer(it, GLFrameBufferAttachType.DEPTH, depthBuffer)

            val texture = gl.createTexture2d(this.screenSize.x.toInt(), this.screenSize.y.toInt(), GLInternalFormat.RGBA8, GLFormat.RGBA)
            gl.filterTexture(GLFilterType.Nearest)
            gl.attachTexture2d(texture.id, GLFrameBufferAttachType.COLOR0)

            gl.frameBufferTexture(GLFrameBufferAttachType.COLOR0, texture)

            virtualView = Square(this.screenSize, texture).also { square ->
                square.position = Vector3(this.screenSize / 2.0f, 0.0f)
                square.scale = Vector3(1.0f, -1.0f, 1.0f)
                square.bind()
            }
        }
        virtualViewShaderProgram = NoColorsShaderProgram()
        virtualCamera = this.createCamera2D().also { }

        beforeMillis = Time.milliseconds()
        currentScene = AppScene()
    }

    fun draw() {
        // transit view
        val sceneFactory = this.sceneFactory
        if (sceneFactory != null) {
            currentScene?.destroyed()
            touchController.clearAll()
            currentScene = sceneFactory.invoke()
            this.sceneFactory = null
        }

        // calc frame seconds
        val now = Time.milliseconds()
        val millisPerFrame = now - beforeMillis
        beforeMillis = now
        val delta = millisPerFrame.toFloat() / 1000.0f

        touchControllerEntity.update(delta)
        timerEventController.update(delta)

        frameBuffer?.let { gl.bindFrameBuffer(it) }
        gl.viewPort(0, 0, screenSize.x.toInt(), screenSize.y.toInt())
        animator.update(delta)
        currentScene?.draw(delta)

        gl.bindDefaultFrameBuffer()
        gl.viewPort(viewport.origin.x.toInt(), viewport.origin.y.toInt(), viewport.size.x.toInt(), viewport.size.y.toInt())

        val shaderProgram = virtualViewShaderProgram
        val view = virtualView
        val camera = virtualCamera
        if (shaderProgram != null && view != null && camera != null) {
            view.draw(delta, shaderProgram, camera)
        }

        // release bindings
        gl.useProgram(0)
        gl.bindVBO(0)
        gl.disableVertexPointer(GLAttribLocation.ATTRIBUTE_COLOR)
        gl.disableVertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD)
        gl.disableVertexPointer(GLAttribLocation.ATTRIBUTE_POSITION)
    }

    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        currentScene?.reshape(x, y, width, height)
    }

    fun createOrthographic(): Matrix4 {
        return Camera.createOrthographic(0.0f, screenSize.x, 0.0f, screenSize.y, -10000.0f, 10000.0f)
    }

    fun createCamera2D() = Camera2D().apply {
        projectionMatrix = createOrthographic()
        update()
    }

    fun createCamera3D() = Camera3D().apply {
        projectionMatrix = createOrthographic()
        update()
    }

    fun runScene(sceneFactory: () -> Scene) {
        this.sceneFactory = sceneFactory
    }
}