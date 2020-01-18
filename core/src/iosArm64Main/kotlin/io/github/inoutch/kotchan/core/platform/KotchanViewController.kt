package io.github.inoutch.kotchan.core.platform

import eaglview.EAGLView
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.KotchanStartupConfig
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.utility.Timer
import io.github.inoutch.kotlin.gl.api.GL_COLOR_BUFFER_BIT
import io.github.inoutch.kotlin.gl.api.gl
import io.github.inoutch.kotlin.vulkan.api.VkInstance
import io.github.inoutch.kotlin.vulkan.api.VkSurface
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.MutableProperty
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.objcPtr
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.useContents
import kotlinx.cinterop.value
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import platform.EAGL.EAGLContext
import platform.EAGL.kEAGLRenderingAPIOpenGLES2
import platform.EAGL.presentRenderbuffer
import platform.EAGL.renderbufferStorage
import platform.Foundation.NSDefaultRunLoopMode
import platform.Foundation.NSRunLoop
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CADisplayLink
import platform.QuartzCore.CAEAGLLayer
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewController
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
import vulkan_ios.VK_STRUCTURE_TYPE_IOS_SURFACE_CREATE_INFO_MVK
import vulkan_ios.VkIOSSurfaceCreateInfoMVK

@ExperimentalUnsignedTypes
class KotchanViewController(
        private val viewController: UIViewController,
        private val startupConfig: KotchanStartupConfig,
        private val viewControllerConfig: KotchanViewControllerConfig = KotchanViewControllerConfig()
) {
    lateinit var windowSize: Vector2I
        private set

    lateinit var viewportSize: Vector2I
        private set

    val view: UIView
        get() = viewController.view

    private lateinit var displayLink: CADisplayLink

    private lateinit var eagleContext: KotchanEAGLContext

    private lateinit var engine: KotchanEngine

    private var lastTime: Long = 0

    fun viewDidLoad() {
        displayLink = CADisplayLink.displayLinkWithTarget(this.viewController, NSSelectorFromString("render:"))
        displayLink.preferredFramesPerSecond = startupConfig.fps.toLong()
        displayLink.addToRunLoop(NSRunLoop.mainRunLoop, NSDefaultRunLoopMode)

        windowSize = UIScreen.mainScreen().nativeBounds().useContents {
            Vector2I(size.width.toInt(), size.height.toInt())
        }
        viewportSize = UIScreen.mainScreen().bounds().useContents {
            Vector2I(size.width.toInt(), size.height.toInt())
        }

        engine = KotchanEngine(startupConfig)
        val kotchanViewController = this
        runBlocking {
            engine.run(KotchanPlatformBridgeConfig(kotchanViewController, viewControllerConfig))
        }
        initWithEAGL()

        lastTime = Timer.milliseconds()
    }

    fun render() {
        GlobalScope.launch(MainLoopDispatcher) {
            val now = Timer.milliseconds()
            renderWithEAGL {
                gl.clearColor(1.0f, 0.0f, 0.0f, 1.0f)
                gl.clear(GL_COLOR_BUFFER_BIT)

                engine.render((now - lastTime) / 1000.0f)
            }
            lastTime = now
        }
    }

    fun createSurface(surface: MutableProperty<VkSurface>, instance: VkInstance) = memScoped {
        val createInfo = alloc<VkIOSSurfaceCreateInfoMVK>()
        createInfo.sType = VK_STRUCTURE_TYPE_IOS_SURFACE_CREATE_INFO_MVK
        createInfo.pNext = null
        createInfo.flags = 0u
        createInfo.pView = viewController.view.objcPtr().toLong().toCPointer()
        vk.createIOSSurfaceMVK(instance, createInfo, surface)
    }

    private fun initWithEAGL() {
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
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorRenderbuffer);

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

    private suspend fun renderWithEAGL(scope: suspend () -> Unit) {
        engine.platform.eagleContext.setContext()
        engine.platform.eagleContext.setFramebuffer()

        scope()

        engine.platform.eagleContext.presentFramebuffer()
    }
}
