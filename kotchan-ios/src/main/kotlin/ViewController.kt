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

import kotchan.Engine
import kotchan.event.TouchEvent

import utility.type.Size
import utility.type.Vector2
import extension.*

@ExportObjCClass
class ViewController : GLKViewController, GKGameCenterControllerDelegateProtocol {
    private lateinit var engine: Engine
    private val touchEvents = mutableMapOf<ObjCObject, TouchEvent>()

    constructor(aDecoder: NSCoder) : super(aDecoder)

    override fun initWithCoder(aDecoder: NSCoder) = initBy(ViewController(aDecoder))

    private lateinit var context: EAGLContext

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
        engine = Engine()
        engine.init(
                Size(screenWidth.toFloat(), screenHeight.toFloat()),
                Size(screenWidth.toFloat(), screenHeight.toFloat()))
    }

    override fun glkView(view: GLKView, drawInRect: CValue<CGRect>) {
        engine.render(0.0f)
    }

    override fun gameCenterViewControllerDidFinish(gameCenterViewController: GKGameCenterViewController) {}

    override fun touchesBegan(touches: NSSet, withEvent: UIEvent?) {
        super.touchesBegan(touches, withEvent)

        val list = touches.objectEnumerator().toList<ObjCObject>().map {
            val touch = it.reinterpret<UITouch>()
            val point = touch.locationInView(this.view).useContents { Vector2(x.toFloat(), y.toFloat()) }
            TouchEvent(point).also { touchEvents[touch] = it }
        }
        engine.touchInterface.onTouchesBegan(list)
    }

    override fun touchesMoved(touches: NSSet, withEvent: UIEvent?) {
        super.touchesMoved(touches, withEvent)

        val list = touches.objectEnumerator().toList<ObjCObject>().mapNotNull {
            val touch = it.reinterpret<UITouch>()
            touchEvents[touch]
        }
        engine.touchInterface.onTouchesMoved(list)
    }

    override fun touchesEnded(touches: NSSet, withEvent: UIEvent?) {
        super.touchesEnded(touches, withEvent)

        val list = touches.objectEnumerator().toList<ObjCObject>().mapNotNull {
            val touch = it.reinterpret<UITouch>()
            val touchEvent = touchEvents[touch]
            touch?.let { touchEvents.remove(it) }
            touchEvent
        }
        engine.touchInterface.onTouchesEnded(list)
    }

    override fun touchesCancelled(touches: NSSet, withEvent: UIEvent?) {
        super.touchesCancelled(touches, withEvent)
        touchEvents.clear()
        engine.touchInterface.onTouchesCancelled()
    }
}
