package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.utility.Timer
import kotlinx.coroutines.delay
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.GLFW_CLIENT_API
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import org.lwjgl.glfw.GLFW.GLFW_NO_API
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.glfw.GLFW.GLFW_RESIZABLE
import org.lwjgl.glfw.GLFW.GLFW_TRUE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwGetFramebufferSize
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback
import org.lwjgl.glfw.GLFW.glfwShowWindow
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryUtil

class GLFWLauncher(
        private val engine: KotchanEngine,
        private val config: GLFWLauncherConfig
) {
    private val window: Long

    private val useVulkan: Boolean

    private var cursorPoint: Vector2I = Vector2I.Zero

    init {
        val common = config.common
        val platform = config.platform as? GLFWPlatformConfig ?: GLFWPlatformConfig()
        val windowSize = platform.windowSize ?: config.common.windowSize

        check(glfwInit()) { "Unable to initialize GLFW" }
        useVulkan = common.useVulkanIfSupported && GLFWVulkan.glfwVulkanSupported()

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, if (platform.resizable) GLFW_TRUE else GLFW_FALSE)

        window = glfwCreateWindow(
                windowSize.x,
                windowSize.y,
                platform.windowTitle ?: common.applicationName,
                MemoryUtil.NULL,
                MemoryUtil.NULL
        )

        val viewportWidth = BufferUtils.createIntBuffer(1)
        val viewportHeight = BufferUtils.createIntBuffer(1)
        glfwGetFramebufferSize(window, viewportWidth, viewportHeight)
        val viewportSize = Vector2I(viewportWidth[0], viewportHeight[0])

        engine.viewportSize = viewportSize

        setInputCallbacks(engine.startupConfig.windowSize)
        glfwShowWindow(window)
    }

    suspend fun startAnimation() {
        var lastTime = Timer.milliseconds()
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()

            engine.render((Timer.milliseconds() - lastTime) / 1000.0f)

            val now = Timer.milliseconds()
            val ideal = (lastTime + 1.0 / config.common.fps * 1000.0).toLong()
            if (now < ideal) {
                delay(ideal - now)
            }
            lastTime = now
        }
    }

    private fun setInputCallbacks(viewportSize: Vector2I) {
        glfwSetCursorPosCallback(window) { _, xpos, ypos ->
            cursorPoint = Vector2I(xpos.toInt(), viewportSize.y - ypos.toInt())
        }
        glfwSetMouseButtonCallback(window) { _: Long, button: Int, action: Int, _: Int ->
            if (button != GLFW_MOUSE_BUTTON_LEFT) {
                return@glfwSetMouseButtonCallback
            }
            when (action) {
                GLFW_PRESS -> {
                }
                GLFW_RELEASE -> {
                }
            }
        }
    }
}
