import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.controller.touch.TouchEvent
import io.github.inoutch.kotchan.ios.DefaultConfig
import io.github.inoutch.kotchan.utility.type.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.EAGL.*
import platform.Foundation.*
import platform.GameKit.*
import platform.GLKit.*
import platform.UIKit.*

@ExportObjCClass
class ViewController : GLKViewController, GKGameCenterControllerDelegateProtocol {

    private lateinit var core: KotchanCore

    private val touchEvents = mutableMapOf<ObjCObject, TouchEvent>()

    private lateinit var context: EAGLContext

    private lateinit var windowSize: Point

    private lateinit var viewSize: Point

    private lateinit var windowRatio: Vector2

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
        core = KotchanCore(config, windowSize)
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
            val point = touch.locationInView(this.view)
                    .useContents { Point((x * windowRatio.x).toInt(), (windowSize.y - y * windowRatio.y).toInt()) }
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
                    .useContents { Point((x * windowRatio.x).toInt(), (windowSize.y - y * windowRatio.y).toInt()) }
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
                    .useContents { Point((x * windowRatio.x).toInt(), (windowSize.y - y * windowRatio.y).toInt()) }
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
