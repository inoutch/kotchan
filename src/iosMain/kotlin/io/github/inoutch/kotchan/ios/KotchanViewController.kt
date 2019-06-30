import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.controller.touch.TouchEvent
import io.github.inoutch.kotchan.ios.DefaultConfig
import io.github.inoutch.kotchan.utility.graphic.vulkan.VK
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkInstance
import io.github.inoutch.kotchan.utility.graphic.vulkan.vkCreateIOSSurfaceMVK
import io.github.inoutch.kotchan.utility.type.*
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.QuartzCore.CADisplayLink
import platform.UIKit.*
import vulkan.VK_STRUCTURE_TYPE_IOS_SURFACE_CREATE_INFO_MVK
import vulkan.VkIOSSurfaceCreateInfoMVK

@ExperimentalUnsignedTypes
class KotchanViewController(private val viewController: UIViewController) {

    private lateinit var displayLink: CADisplayLink

    private lateinit var core: KotchanCore

    private val touchEvents = mutableMapOf<ObjCObject, TouchEvent>()

    private lateinit var windowSize: Point

    private lateinit var viewSize: Point

    private lateinit var windowRatio: Vector2

    fun viewDidLoad() {

        this.viewController.view.contentScaleFactor = UIScreen.mainScreen.nativeScale

        displayLink = CADisplayLink.displayLinkWithTarget(viewController, NSSelectorFromString("render:"))
        displayLink.preferredFramesPerSecond = 60
        displayLink.addToRunLoop(NSRunLoop.currentRunLoop, NSDefaultRunLoopMode)

        viewSize = UIScreen.mainScreen().bounds().useContents {
            Point(size.width.toInt(), size.height.toInt())
        }

        windowSize = UIScreen.mainScreen().nativeBounds().useContents {
            Point(size.width.toInt(), size.height.toInt())
        }

        windowRatio = Vector2(
                windowSize.x.toFloat() / viewSize.x,
                windowSize.y.toFloat() / viewSize.y)

        val config = DefaultConfig.config ?: throw Error("KotchanEngineConfig is not applied")
        val vk = VK(config.appName,
                windowSize,
                emptyList(),
                listOf("VK_KHR_surface", "VK_MVK_ios_surface"),
                emptyList(),
                listOf("VK_KHR_swapchain")) { createSurface(it) }

        core = KotchanCore(config, windowSize)
        core.applyVK(vk)
        core.init()
    }

    fun render() {
        core.draw()
    }

    fun touchesBegan(touches: Set<*>) {
        val list = touches.mapNotNull { touch ->
            if (touch !is UITouch) {
                return@mapNotNull null
            }
            val point = touch.locationInView(this.viewController.view)
                    .useContents { Point((x * windowRatio.x).toInt(), (windowSize.y - y * windowRatio.y).toInt()) }
            TouchEvent(point).also { touchEvents[touch] = it }
        }
        core.touchEmitter.onTouchesBegan(list)
    }

    fun touchesMoved(touches: Set<*>) {
        val list = touches.mapNotNull { touch ->
            if (touch !is UITouch) {
                return@mapNotNull null
            }
            val touchEvent = touchEvents[touch] ?: return@mapNotNull null
            touchEvent.point = touch.locationInView(this.viewController.view)
                    .useContents { Point((x * windowRatio.x).toInt(), (windowSize.y - y * windowRatio.y).toInt()) }
            touchEvent
        }
        core.touchEmitter.onTouchesMoved(list)
    }

    fun touchesEnded(touches: Set<*>) {
        val list = touches.mapNotNull { touch ->
            if (touch !is UITouch) {
                return@mapNotNull null
            }
            val touchEvent = touchEvents[touch] ?: return@mapNotNull null
            touchEvent.point = touch.locationInView(this.viewController.view)
                    .useContents { Point((x * windowRatio.x).toInt(), (windowSize.y - y * windowRatio.y).toInt()) }
            touchEvent
        }
        core.touchEmitter.onTouchesEnded(list)
    }

    fun touchesCancelled() {
        touchEvents.clear()
        core.touchEmitter.onTouchesCancelled()
    }

    private fun createSurface(instance: VkInstance) = memScoped {
        val nativeCreateInfo = alloc<VkIOSSurfaceCreateInfoMVK>()
        nativeCreateInfo.sType = VK_STRUCTURE_TYPE_IOS_SURFACE_CREATE_INFO_MVK
        nativeCreateInfo.pNext = null
        nativeCreateInfo.flags = 0u
        nativeCreateInfo.pView = viewController.view.objcPtr().toLong().toCPointer()

        vkCreateIOSSurfaceMVK(instance, nativeCreateInfo)
    }
}
