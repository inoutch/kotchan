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

@ExportObjCClass
class ViewController : GLKViewController, GKGameCenterControllerDelegateProtocol {
    private lateinit var engine: Engine

    constructor(aDecoder: NSCoder) : super(aDecoder)

    override fun initWithCoder(aDecoder: NSCoder) = initBy(ViewController(aDecoder))

    private lateinit var context: EAGLContext

    override fun viewDidLoad() {
        this.context = EAGLContext(kEAGLRenderingAPIOpenGLES3)

        val view = this.view.reinterpret<GLKView>()
        view.context = this.context
        view.drawableDepthFormat = GLKViewDrawableDepthFormat24
        view.drawableMultisample = GLKViewDrawableMultisample4X

        EAGLContext.setCurrentContext(this.context)

        engine = Engine()
        engine.init(0, 0, 300, 300)
    }

    override fun glkView(view: GLKView, drawInRect: CValue<CGRect>) {
        //glClearColor(1.0f, 1.0f, 0.0f, 1.0f)
        //glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        engine.render(0.0f)
    }

    override fun gameCenterViewControllerDidFinish(gameCenterViewController: GKGameCenterViewController) {}

    @ObjCAction
    fun handlePanGesture(sender: UIPanGestureRecognizer) {}
}
