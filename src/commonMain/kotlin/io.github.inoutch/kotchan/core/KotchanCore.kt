package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.core.controller.event.listener.EventController
import io.github.inoutch.kotchan.core.controller.event.listener.TimerEventController
import io.github.inoutch.kotchan.core.controller.touch.TouchControllerEntity
import io.github.inoutch.kotchan.core.logger.logger
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.utility.io.File
import io.github.inoutch.kotchan.utility.time.Timer
import io.github.inoutch.kotchan.utility.type.*
import io.github.inoutch.kotchan.core.graphic.animator.Animator
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.camera.Camera3D

class KotchanCore(
        private val config: KotchanEngine.Config,
        val vk: KotchanVk) {

    companion object {
        const val KOTCHAN_ENGINE_NAME = "kotchan-engine"
        val instance: KotchanCore get() = KotchanInstance.manager().get(KOTCHAN_ENGINE_NAME) as KotchanCore
    }

    private var currentScene: Scene? = null

    private var sceneFactory: (() -> Scene)? = null

    private val touchControllerEntity = TouchControllerEntity()

    private var beforeMillis: Long = 0

//    val textureManager = TextureManager(gl)

    val file = File()

    val touchEmitter = touchControllerEntity

    val touchController = touchControllerEntity

    val eventController = EventController()

    val timerEventController = TimerEventController()

    val animator = Animator()

    val windowSize: Point
        get() = vk.swapchainRecreator.extent

    var screenSize = config.screenSize
        private set

    var viewport = PointRect()
        private set

    val logLevel = config.logLevel

    init {
        KotchanInstance.manager().add(KOTCHAN_ENGINE_NAME, this)
    }

    fun init() {
        logger.init(config.logLevel)

        this.screenSize = calcScreenSize()
        this.viewport = calcViewport()

        beforeMillis = Timer.milliseconds()
        currentScene = config.initScene()
    }

    fun draw() {

        // transit view
        this.sceneFactory?.let { clearScreenFactory(it) }

        // calc frame seconds
        val delta = calcDelta()

        touchControllerEntity.update(delta)
//        timerEventController.update(delta)

        animator.update(delta)
        currentScene?.draw(delta)

        vk.draw()
        // TODO:
//        gl.bindDefaultFrameBuffer()
//        gl.viewPort(viewport.origin.x, viewport.origin.y, viewport.size.x, viewport.size.y)

        // release bindings
        // TODO:
//        gl.useProgram(0)
//        gl.bindVBO(0)
//        gl.disableVertexPointer(GLAttribLocation.ATTRIBUTE_COLOR)
//        gl.disableVertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD)
//        gl.disableVertexPointer(GLAttribLocation.ATTRIBUTE_POSITION)
    }

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
        currentScene?.destroyed()
        touchController.clearAll()

        currentScene = sceneFactory.invoke()

        this.sceneFactory = null
    }
}
