package io.github.inoutch.kotchan.jvm

import io.github.inoutch.kotchan.utility.Timer
import kotlin.math.max
import kotlinx.coroutines.delay
import org.lwjgl.glfw.GLFW.GLFW_CLIENT_API
import org.lwjgl.glfw.GLFW.GLFW_NO_API
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryUtil

class GLFWLauncher(
        private val config: GLFWLauncherConfig
) {
    private val window: Long

    init {
        check(glfwInit()) { "Failed to initialize GLFW" }

        if (!GLFWVulkan.glfwVulkanSupported()) {
            throw IllegalStateException("Unsupported vulkan")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)

        window = glfwCreateWindow(
                config.nativeScreenSize.x,
                config.nativeScreenSize.y,
                config.windowName,
                MemoryUtil.NULL,
                MemoryUtil.NULL)
    }

    suspend fun run() {
        var lastTime = Timer.milliseconds()
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()

            config.update()
            config.render()

            val now = Timer.milliseconds()
            val ideal = (lastTime + 1.0 / config.fps * 1000.0).toLong()
            delay(max(0, ideal - now))
            lastTime = now
        }
    }
}
