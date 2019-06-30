import kotlinx.cinterop.*
import platform.Foundation.NSCoder
import platform.QuartzCore.CADisplayLink
import platform.UIKit.*

@ExperimentalUnsignedTypes
@ExportObjCClass
class ViewController : UIViewController {

    @ExperimentalUnsignedTypes
    private val viewController = KotchanViewController(this)

    @OverrideInit
    constructor(coder: NSCoder) : super(coder)

    override fun viewDidLoad() {
        super.viewDidLoad()
        viewController.viewDidLoad()
    }

    @ObjCAction
    fun render(sender: CADisplayLink) {
        viewController.render()
    }

    override fun touchesBegan(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesBegan(touches, withEvent)
        viewController.touchesBegan(touches)
    }

    override fun touchesMoved(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesMoved(touches, withEvent)
        viewController.touchesMoved(touches)
    }

    override fun touchesEnded(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesEnded(touches, withEvent)
        viewController.touchesEnded(touches)
    }

    override fun touchesCancelled(touches: Set<*>, withEvent: UIEvent?) {
        super.touchesCancelled(touches, withEvent)
        viewController.touchesCancelled()
    }
}
