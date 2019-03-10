package io.github.inoutch.kotchan.jvm

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.KotchanVk
import io.github.inoutch.kotchan.extension.toStringList
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkSurface
import io.github.inoutch.kotchan.utility.graphic.vulkan.checkError
import io.github.inoutch.kotchan.utility.io.File
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryUtil
import java.nio.LongBuffer

class GLFWLauncher(config: KotchanEngine.Config) : GLFWKeyCallback() {
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
        glfwSetKeyCallback(window, this)

        val requiredExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions()
                ?: throw Error("Failed to find list of required Vulkan extensions")

        val vk = KotchanVk(config, nativeSize, listOf(), requiredExtensions.toStringList(), listOf(), listOf("VK_KHR_swapchain")) {
            memScoped {
                val surface = MemoryUtil.memAllocLong(1)
                checkError(GLFWVulkan.glfwCreateWindowSurface(it.native, window, null, surface))
                VkSurface().apply { init(surface.get(0)) }
            }
        }

        core = KotchanCore(config, nativeSize, vk)

        glfwShowWindow(window)
    }

    fun run() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()

            core.draw()
        }
    }

    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {}
}
