package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.core.controller.event.listener.EventController
import io.github.inoutch.kotchan.core.controller.event.listener.TimerEventController
import io.github.inoutch.kotchan.core.controller.touch.TouchControllerEntity
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.utility.io.File
import io.github.inoutch.kotchan.utility.time.Timer
import io.github.inoutch.kotchan.utility.type.*
import io.github.inoutch.kotchan.core.graphic.animator.Animator
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.camera.Camera3D
import io.github.inoutch.kotchan.core.graphic.Api
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.core.logger.Logger
import io.github.inoutch.kotchan.core.resource.ResourceManager.Companion.resourceManager
import io.github.inoutch.kotchan.core.tool.TextureCacheManager
import io.github.inoutch.kotchan.utility.graphic.gl.GL
import io.github.inoutch.kotchan.utility.graphic.vulkan.VK

class KotchanCore(
        private val config: KotchanEngine.Config,
        actualSizeWindowSize: Point) {

    companion object {
        const val KOTCHAN_ENGINE_NAME = "kotchan-engine"
        const val KOTCHAN_LOGGER = "kotchan-logger"
        const val KOTCHAN_RESOURCE_SHADER_SIMPLE = "shader/kotchan/simple"
        const val KOTCHAN_RESOURCE_MATERIAL_PLAIN = "material/kotchan/plain"

        val instance: KotchanCore get() = KotchanInstance.manager().get(KOTCHAN_ENGINE_NAME) as KotchanCore
        val logger: Logger get() = KotchanInstance.manager().get(KOTCHAN_LOGGER) as Logger
    }

    private var currentScene: Scene? = null

    private var sceneFactory: (() -> Scene)? = null

    private val touchControllerEntity = TouchControllerEntity()

    private var beforeMillis: Long = 0

    // external utilities
    val file = File()

    var gl: GL? = null
        private set

    var vk: VK? = null
        private set

    lateinit var graphicsApi: Api

    val textureCacheManager = TextureCacheManager()

    val touchEmitter = touchControllerEntity

    val touchController = touchControllerEntity

    val eventController = EventController()

    val timerEventController = TimerEventController()

    val animator = Animator()

    val windowSize = actualSizeWindowSize

    var screenSize = config.screenSize
        private set

    var viewport = PointRect()
        private set

    init {
        // add engine instance to manager
        KotchanInstance.manager().add(KOTCHAN_ENGINE_NAME, this)
        KotchanInstance.manager().add(KOTCHAN_LOGGER, config.loggerFactory?.create() ?: Logger())
    }

    fun init() {
        logger.init(config.logLevel)
        graphicsApi = Api(this.vk, this.gl)

        this.screenSize = calcScreenSize()
        this.viewport = calcViewport()

        beforeMillis = Timer.milliseconds()

        sceneFactory = {
            // Load preset resources
            this.loadPresetResources()
            config.initScene()
        }
    }

    fun draw() {
        // initialize per frame for graphic api
        graphicsApi.begin()

        // transit view
        this.sceneFactory?.let { clearScreenFactory(it) }

        // calc frame seconds
        val delta = calcDelta()

        touchControllerEntity.update(delta)
        timerEventController.update(delta)

        animator.update(delta)

        currentScene?.draw(delta)

        graphicsApi.end()
    }

    fun applyGL(gl: GL) {
        this.gl = gl
    }

    fun applyVK(vk: VK) {
        this.vk = vk
    }

    // TODO: implement reshape
    fun reshape(x: Int, y: Int, width: Int, height: Int) {
        currentScene?.reshape(x, y, width, height)
    }

    fun createOrthographic(): Matrix4 {
        return Camera.createOrthographic(0.0f, screenSize.x.toFloat(), 0.0f, screenSize.y.toFloat(), -10000.0f, 10000.0f)
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

    private fun calcDelta(): Float {
        val now = Timer.milliseconds()
        val millisPerFrame = now - beforeMillis

        beforeMillis = now

        return millisPerFrame.toFloat() / 1000.0f
    }

    private fun calcScreenSize(): Point {
        val windowRatio = windowSize.y.toFloat() / windowSize.x.toFloat()
        return when (config.screenType) {
            ScreenType.EXTEND -> screenSize
            ScreenType.FIX_WIDTH -> Point(screenSize.x, (screenSize.x * windowRatio).toInt())
            ScreenType.FIX_HEIGHT -> Point((screenSize.y / windowRatio).toInt(), screenSize.y)
            ScreenType.BORDER -> screenSize
        }
    }

    private fun calcViewport(): PointRect {
        val windowSize = vk?.swapchainRecreator?.extent ?: windowSize
        val windowRatio = windowSize.y / windowSize.x
        return when (config.screenType) {
            ScreenType.EXTEND -> PointRect(Point(), windowSize)
            ScreenType.FIX_WIDTH -> PointRect(Point(), windowSize)
            ScreenType.FIX_HEIGHT -> PointRect(Point(), windowSize)
            ScreenType.BORDER -> {
                val screenRatio = screenSize.y / screenSize.x
                return when {
                    windowRatio > screenRatio -> {
                        // top-bottom
                        val border = (windowSize.y - windowSize.x * screenRatio) / 2.0f
                        PointRect(Point(0.0f, border), Point(windowSize.x.toFloat(), windowSize.y - border))
                    }
                    windowRatio < screenRatio -> {
                        // right-left
                        val border = (windowSize.x - windowSize.y / screenRatio) / 2.0f
                        PointRect(Point(border, 0.0f), Point(windowSize.x - border, windowSize.y.toFloat()))
                    }
                    else -> PointRect(Point(), windowSize)
                }
            }
        }
    }

    private fun clearScreenFactory(sceneFactory: () -> Scene) {
        currentScene?.dispose()

        touchController.clearAll()

        currentScene = sceneFactory.invoke()
        this.sceneFactory = null
    }

    private fun loadPresetResources() {
        val simpleShaderProgram = SimpleShaderProgram()
        resourceManager.delegate(KOTCHAN_RESOURCE_SHADER_SIMPLE, simpleShaderProgram)

        resourceManager.delegate(KOTCHAN_RESOURCE_MATERIAL_PLAIN, Material(Material.Config(simpleShaderProgram), listOf(Texture.emptyTexture())))
    }
}
