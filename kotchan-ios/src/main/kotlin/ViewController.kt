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
import utility.type.Size

@ExportObjCClass
class ViewController : GLKViewController, GKGameCenterControllerDelegateProtocol {
    private lateinit var engine: Engine

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

    @ObjCAction
    fun handlePanGesture(sender: UIPanGestureRecognizer) {
    }
}
