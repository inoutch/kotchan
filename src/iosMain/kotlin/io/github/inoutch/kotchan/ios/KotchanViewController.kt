package io.github.inoutch.kotchan.ios

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.controller.touch.TouchEvent
import io.github.inoutch.kotchan.utility.type.Point
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.EAGL.*
import platform.Foundation.*
import platform.GameKit.*
import platform.GLKit.*
import platform.UIKit.*

@ExportObjCClass
open class KotchanViewController : GLKViewController, GKGameCenterControllerDelegateProtocol {

    private lateinit var core: KotchanCore

    private val touchEvents = mutableMapOf<ObjCObject, TouchEvent>()

    private lateinit var context: EAGLContext

    private var width: Float = 0.0f

    private var height: Float = 0.0f

    private var viewWidth = 0.0f

    private var viewHeight = 0.0f

    private var widthRatio = 0.0f

    private var heightRatio = 0.0f

    @OverrideInit
    constructor(coder: NSCoder) : super(coder)

    override fun viewDidLoad() {
        this.context = EAGLContext(kEAGLRenderingAPIOpenGLES3)

        val view = this.view as GLKView
        view.context = this.context
        view.drawableDepthFormat = GLKViewDrawableDepthFormat16
        view.bindDrawable()

        this.preferredFramesPerSecond = 60
        EAGLContext.setCurrentContext(this.context)

        UIScreen.mainScreen().bounds.useContents {
            viewWidth = size.width.toFloat()
            viewHeight = size.height.toFloat()
        }

        UIScreen.mainScreen().nativeBounds.useContents {
            width = size.width.toFloat()
            height = size.height.toFloat()
        }
        widthRatio = width / viewWidth
        heightRatio = height / viewHeight

        core = KotchanCore(DefaultConfig.config ?: throw Error("KotchanEngineConfig is not applied"))
        core.init()
    }

    override fun glkView(view: GLKView, drawInRect: CValue<CGRect>) {
        core.draw()
    }

    override fun gameCenterViewControllerDidFinish(gameCenterViewController: GKGameCenterViewController) {}

    override fun touchesBegan(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesBegan(touches, withEvent)

        val list = touches.mapNotNull { touch ->
            if (touch !is UITouch) {
                return@mapNotNull null
            }
            val point = touch.locationInView(this.view).useContents { Point(x.toFloat() * widthRatio, height - y.toFloat() * heightRatio) }
            TouchEvent(point).also { touchEvents[touch] = it }
        }
        core.touchEmitter.onTouchesBegan(list)
    }

    override fun touchesMoved(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesMoved(touches, withEvent)

        val list = touches.mapNotNull { touch ->
            if (touch !is UITouch) {
                return@mapNotNull null
            }
            val touchEvent = touchEvents[touch] ?: return@mapNotNull null
            touchEvent.point = touch.locationInView(this.view)
                    .useContents { Point(x.toFloat() * widthRatio, height - y.toFloat() * heightRatio) }
            touchEvent
        }
        core.touchEmitter.onTouchesMoved(list)
    }

    override fun touchesEnded(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesEnded(touches, withEvent)

        val list = touches.mapNotNull { touch ->
            if (touch !is UITouch) {
                return@mapNotNull null
            }
            val touchEvent = touchEvents[touch] ?: return@mapNotNull null
            touchEvent.point = touch.locationInView(this.view)
                    .useContents { Point(x.toFloat() * widthRatio, height - y.toFloat() * heightRatio) }
            touchEvent
        }
        core.touchEmitter.onTouchesEnded(list)
    }

    override fun touchesCancelled(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesCancelled(touches, withEvent)
        touchEvents.clear()
        core.touchEmitter.onTouchesCancelled()
    }
}