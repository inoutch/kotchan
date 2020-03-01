package io.github.inoutch.kotchan.core

import eaglview.EAGLView
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.gl.GLContext
import io.github.inoutch.kotchan.core.graphic.compatible.vk.VKContext
import io.github.inoutch.kotchan.core.platform.KotchanEAGLContext
import io.github.inoutch.kotchan.core.platform.KotchanPlatformBridgeConfig
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.vulkan.api.VkApplicationInfo
import io.github.inoutch.kotlin.vulkan.api.VkInstanceCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.EAGL.EAGLContext
import platform.EAGL.kEAGLRenderingAPIOpenGLES2
import platform.EAGL.renderbufferStorage
import platform.QuartzCore.CAEAGLLayer
import platform.UIKit.UIScreen
import platform.UIKit.addSubview
import platform.UIKit.contentScaleFactor
import platform.gles2.GL_COLOR_ATTACHMENT0
import platform.gles2.GL_FRAMEBUFFER
import platform.gles2.GL_FRAMEBUFFER_COMPLETE
import platform.gles2.GL_RENDERBUFFER
import platform.gles2.GL_RENDERBUFFER_HEIGHT
import platform.gles2.GL_RENDERBUFFER_WIDTH
import platform.gles2.glBindFramebuffer
import platform.gles2.glBindRenderbuffer
import platform.gles2.glCheckFramebufferStatus
import platform.gles2.glFramebufferRenderbuffer
import platform.gles2.glGenFramebuffers
import platform.gles2.glGenRenderbuffers
import platform.gles2.glGetRenderbufferParameteriv
import platform.glescommon.GLintVar
import platform.glescommon.GLuintVar

@ExperimentalUnsignedTypes
actual class KotchanPlatform actual constructor(
    engine: KotchanEngine,
    platformConfig: KotchanPlatformConfig?
) {
    val context: Context

    lateinit var eagleContext: KotchanEAGLContext
        private set

    init {
        check(platformConfig is KotchanPlatformBridgeConfig)

        val useVulkan = engine.startupConfig.useVulkanIfSupported
        if (useVulkan) {
            val vulkanConfig = platformConfig.viewControllerConfig.vulkanConfig
            val applicationInfo = VkApplicationInfo(
                    VkStructureType.VK_STRUCTURE_TYPE_APPLICATION_INFO,
                    vulkanConfig.applicationName ?: engine.startupConfig.applicationName,
                    vulkanConfig.version,
                    vulkanConfig.engineName ?: engine.startupConfig.applicationName,
                    vulkanConfig.engineVersion,
                    vulkanConfig.apiVersion
            )
            val createInfo = VkInstanceCreateInfo(
                    VkStructureType.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO,
                    0,
                    applicationInfo,
                    vulkanConfig.enableLayerNames,
                    vulkanConfig.enableExtensionNames
                            ?: listOf("VK_KHR_surface", "VK_MVK_ios_surface")
            )
            context = VKContext(
                    createInfo,
                    platformConfig.viewController.windowSize,
                    vulkanConfig.maxFrameInFlight
            ) { surface, instance ->
                platformConfig.viewController.createSurface(surface, instance)
            }
        } else {
            context = GLContext()
            initWithEAGL(platformConfig)
        }
    }

    actual fun createLauncher(): KotchanPlatformLauncher {
        return object : KotchanPlatformLauncher {
            override fun getGraphics(): Context {
                return context
            }

            override suspend fun startAnimation() {
                // Skip
            }
        }
    }

    private fun initWithEAGL(config: KotchanPlatformBridgeConfig) {
        val viewController = config.viewController
        val context = EAGLContext(kEAGLRenderingAPIOpenGLES2)
        EAGLContext.setCurrentContext(context)

        val eaglView = EAGLView(viewController.view.frame)
        viewController.view.addSubview(eaglView)

        eaglView.contentScaleFactor = UIScreen.mainScreen.nativeScale

        val defaultFramebuffer = memScoped {
            val intVar = alloc<GLuintVar>()
            glGenFramebuffers(1, intVar.ptr)
            intVar.value
        }
        glBindFramebuffer(GL_FRAMEBUFFER, defaultFramebuffer)

        val colorRenderbuffer = memScoped {
            val intVar = alloc<GLuintVar>()
            glGenRenderbuffers(1, intVar.ptr)
            intVar.value
        }
        glBindRenderbuffer(GL_RENDERBUFFER, colorRenderbuffer)

        context.renderbufferStorage(GL_RENDERBUFFER, eaglView.layer as CAEAGLLayer)
        val framebufferWidth = memScoped {
            val intVar = alloc<GLintVar>()
            glGetRenderbufferParameteriv(GL_RENDERBUFFER, GL_RENDERBUFFER_WIDTH, intVar.ptr)
            intVar.value
        }
        val framebufferHeight = memScoped {
            val intVar = alloc<GLintVar>()
            glGetRenderbufferParameteriv(GL_RENDERBUFFER, GL_RENDERBUFFER_HEIGHT, intVar.ptr)
            intVar.value
        }
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorRenderbuffer)

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE.toUInt()) {
            println("Failed to make complete framebuffer object ${glCheckFramebufferStatus(GL_FRAMEBUFFER)}")
        }
        eagleContext = KotchanEAGLContext(
                context,
                eaglView,
                defaultFramebuffer,
                colorRenderbuffer,
                Vector2I(framebufferWidth, framebufferHeight)
        )
    }
}
