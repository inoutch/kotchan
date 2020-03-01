package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.KotchanPlatformLauncher
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.gl.GLContext
import io.github.inoutch.kotchan.core.graphic.compatible.vk.VKContext
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.utility.Timer
import io.github.inoutch.kotlin.vulkan.api.VkApplicationInfo
import io.github.inoutch.kotlin.vulkan.api.VkInstanceCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.createWindowSurface
import io.github.inoutch.kotlin.vulkan.extension.toStringList
import kotlinx.coroutines.delay
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
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
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

class GLFWLauncher(
    private val engine: KotchanEngine,
    private val config: GLFWLauncherConfig
) : KotchanPlatformLauncher {
    val context: Context

    private val window: Long

    private val useVulkan: Boolean

    private var cursorPoint: Vector2I = Vector2I.Zero

    init {
        GLFWErrorCallback.createPrint().set()

        val common = config.common
        val platform = config.platform as? GLFWPlatformConfig ?: GLFWPlatformConfig()
        val windowSize = platform.windowSize ?: config.common.windowSize

        check(glfwInit()) { "Unable to initialize GLFW" }
        useVulkan = common.useVulkanIfSupported && GLFWVulkan.glfwVulkanSupported()

        glfwDefaultWindowHints()
        if (useVulkan) {
            glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
        }
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

        engine.initSize(windowSize, viewportSize)

        if (useVulkan) {
            val requiredExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions()
            checkNotNull(requiredExtensions) { "Failed to find list of required Vulkan extensions" }

            val vulkanConfig = platform.vulkanConfig
            val applicationInfo = VkApplicationInfo(
                    VkStructureType.VK_STRUCTURE_TYPE_APPLICATION_INFO,
                    vulkanConfig.applicationName ?: common.applicationName,
                    vulkanConfig.version,
                    vulkanConfig.engineName ?: common.applicationName,
                    vulkanConfig.engineVersion,
                    vulkanConfig.apiVersion
            )
            val createInfo = VkInstanceCreateInfo(
                    VkStructureType.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO,
                    0,
                    applicationInfo,
                    vulkanConfig.enableLayerNames,
                    vulkanConfig.enableExtensionNames ?: requiredExtensions.toStringList()
            )
            context = VKContext(createInfo, windowSize, vulkanConfig.maxFrameInFlight) { surface, instance ->
                createWindowSurface(instance, window, surface)
            }
        } else {
            MemoryStack.stackPush().use {
                val pWidth = it.mallocInt(1)
                val pHeight = it.mallocInt(1)

                GLFW.glfwGetWindowSize(window, pWidth, pHeight)

                val vidmode = checkNotNull(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()))
                GLFW.glfwSetWindowPos(
                        window,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                )
            }
            GLFW.glfwMakeContextCurrent(window)
            GLFW.glfwSwapInterval(1)
            GL.createCapabilities()

            context = GLContext()
        }

        setInputCallbacks(engine.startupConfig.windowSize)
        glfwShowWindow(window)
    }

    override fun getGraphics(): Context {
        return context
    }

    override suspend fun startAnimation() {
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

            if (!useVulkan) {
                glfwSwapBuffers(window)
            }
        }

        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwTerminate()
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
