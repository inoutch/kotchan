import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.CoreMotion.*
import platform.EAGL.*
import platform.Foundation.*
import platform.GameKit.*
import platform.GLKit.*
import platform.UIKit.*
import platform.glescommon.*
import platform.gles3.*

import application.AppConfig
import kotchan.Engine
import kotchan.controller.*

import utility.type.Vector2
import extension.*

@ExportObjCClass
class ViewController : GLKViewController, GKGameCenterControllerDelegateProtocol {
    private lateinit var engine: Engine
    private val touchEvents = mutableMapOf<ObjCObject, TouchEvent>()

    constructor(aDecoder: NSCoder) : super(aDecoder)

    override fun initWithCoder(aDecoder: NSCoder) = initBy(ViewController(aDecoder))

    private lateinit var context: EAGLContext
    private var width: Float = 0.0f
    private var height: Float = 0.0f

    override fun viewDidLoad() {
        this.preferredFramesPerSecond = 60
        this.context = EAGLContext(kEAGLRenderingAPIOpenGLES3)

        val view = this.view.reinterpret<GLKView>()
        view.context = this.context
        view.drawableDepthFormat = GLKViewDrawableDepthFormat24
        view.drawableMultisample = GLKViewDrawableMultisample4X

        EAGLContext.setCurrentContext(this.context)

        val (screenWidth, screenHeight) = view.bounds.useContents {
            size.width to size.height
        }
        width = screenWidth.toFloat()
        height = screenHeight.toFloat()

        engine = Engine()
        engine.init(
                Vector2(width, height),
                AppConfig.SCREEN_SIZE)
    }

    override fun glkView(view: GLKView, drawInRect: CValue<CGRect>) {
        engine.render(0.0f)
    }

    override fun gameCenterViewControllerDidFinish(gameCenterViewController: GKGameCenterViewController) {}

    override fun touchesBegan(touches: NSSet, withEvent: UIEvent?) {
        super.touchesBegan(touches, withEvent)

        val list = touches.objectEnumerator().toList<ObjCObject>().map {
            val touch = it.reinterpret<UITouch>()
            val point = touch.locationInView(this.view).useContents { Vector2(x.toFloat(), height - y.toFloat()) }
            TouchEvent(point).also { touchEvents[touch] = it }
        }
        engine.touchEmitter.onTouchesBegan(list)
    }

    override fun touchesMoved(touches: NSSet, withEvent: UIEvent?) {
        super.touchesMoved(touches, withEvent)

        val list = touches.objectEnumerator().toList<ObjCObject>().mapNotNull {
            val touch = it.reinterpret<UITouch>()
            val touchEvent = touchEvents[touch] ?: return@mapNotNull null
            touchEvent.point = touch.locationInView(this.view)
                    .useContents { Vector2(x.toFloat(), height - y.toFloat()) }
            touchEvent
        }
        engine.touchEmitter.onTouchesMoved(list)
    }

    override fun touchesEnded(touches: NSSet, withEvent: UIEvent?) {
        super.touchesEnded(touches, withEvent)

        val list = touches.objectEnumerator().toList<ObjCObject>().mapNotNull {
            val touch = it.reinterpret<UITouch>()
            val touchEvent = touchEvents[touch] ?: return@mapNotNull null
            touchEvent.point = touch.locationInView(this.view)
                    .useContents { Vector2(x.toFloat(), height - y.toFloat()) }
            touchEvent
        }
        engine.touchEmitter.onTouchesEnded(list)
    }

    override fun touchesCancelled(touches: NSSet, withEvent: UIEvent?) {
        super.touchesCancelled(touches, withEvent)
        touchEvents.clear()
        engine.touchEmitter.onTouchesCancelled()
    }
}
