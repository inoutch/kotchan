package io.github.inoutch.kotchan.jvm

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.controller.keyboard.keyboardController
import io.github.inoutch.kotchan.core.controller.touch.TouchEvent
import io.github.inoutch.kotchan.extension.toStringList
import io.github.inoutch.kotchan.utility.graphic.vulkan.VK
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkSurface
import io.github.inoutch.kotchan.utility.graphic.vulkan.checkError
import io.github.inoutch.kotchan.utility.memScoped
import io.github.inoutch.kotchan.utility.time.Timer
import io.github.inoutch.kotchan.utility.type.Point
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryUtil

class GLFWLauncher(config: KotchanEngine.Config) {
    companion object {
        const val TARGET_FPS = 60
    }

    private val window: Long

    private val core: KotchanCore

    init {
        if (!glfwInit()) {
            throw Error("Failed to initialize GLFW")
        }
        if (!GLFWVulkan.glfwVulkanSupported()) {
            throw Error("Unsupported vulkan")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

        val nativeSize = config.windowSize / 2
        window = glfwCreateWindow(nativeSize.x, nativeSize.y, config.appName, MemoryUtil.NULL, MemoryUtil.NULL)

        val requiredExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions()
                ?: throw Error("Failed to find list of required Vulkan extensions")

        val physicalDeviceLayers = listOf<String>()
        val physicalDeviceExtensions = requiredExtensions.toStringList()
        val vk = VK(
                config.appName,
                nativeSize,
                physicalDeviceLayers,
                physicalDeviceExtensions,
                listOf(),
                listOf("VK_KHR_swapchain")) {
            memScoped {
                val surface = MemoryUtil.memAllocLong(1)
                checkError(GLFWVulkan.glfwCreateWindowSurface(it.native, window, null, surface))
                VkSurface().apply { init(surface.get(0)) }
            }
        }

        core = KotchanCore(config, nativeSize)
        core.applyVK(vk)

        var point = Point.ZERO
        var touch: TouchEvent? = null
        glfwSetCursorPosCallback(window) { _, xpos, ypos ->
            point = Point(xpos.toInt(), nativeSize.y - ypos.toInt())
            val t = touch ?: return@glfwSetCursorPosCallback
            t.point = point
            core.touchEmitter.onTouchesMoved(listOf(t))
        }
        glfwSetMouseButtonCallback(window) { _: Long, button: Int, action: Int, _: Int ->
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                when (action) {
                    GLFW_PRESS -> {
                        val t = TouchEvent(point)
                        core.touchEmitter.onTouchesBegan(listOf(t))
                        touch = t
                    }
                    GLFW_RELEASE -> {
                        val t = touch ?: return@glfwSetMouseButtonCallback
                        core.touchEmitter.onTouchesEnded(listOf(t))
                        touch = null
                    }
                }
            }
        }
        glfwSetKeyCallback(window) { _, key, _, action, _ ->
            if (action == GLFW_PRESS) {
                when (key) {
                    GLFW_KEY_BACKSPACE -> keyboardController.delete()
                    GLFW_KEY_ENTER -> keyboardController.enter()
                }
            }
        }
        glfwSetCharCallback(window) { _, codepoint ->
            keyboardController.input(codepoint.toChar().toString())
        }

        core.init()
        glfwShowWindow(window)
    }

    fun run() {
        var lastTime = Timer.milliseconds()
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()

            core.draw()

            val now = Timer.milliseconds()
            val ideal = (lastTime + 1.0 / TARGET_FPS * 1000.0).toLong()
            if (now < ideal) {
                Thread.sleep(ideal - now)
            }
            lastTime = now
        }
    }
}
