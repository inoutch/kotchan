import io.github.inoutch.kotchan.core.platform.KotchanViewController
import io.github.inoutch.kotchan.example.AppConfig
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSCoder
import platform.QuartzCore.CADisplayLink
import platform.UIKit.UIScreen
import platform.UIKit.UIViewController
import platform.UIKit.contentScaleFactor

@ExperimentalUnsignedTypes
@ExportObjCClass
class ViewController : UIViewController {
    private val kotchanViewController = KotchanViewController(this, AppConfig())

    @Suppress("ConvertSecondaryConstructorToPrimary")
    @OverrideInit
    constructor(coder: NSCoder) : super(coder)

    override fun viewDidLoad() {
        super.viewDidLoad()
        view.contentScaleFactor = UIScreen.mainScreen.nativeScale
        kotchanViewController.viewDidLoad()
    }

    @Suppress("UNUSED_PARAMETER")
    @ObjCAction
    fun render(sender: CADisplayLink) {
        kotchanViewController.render()
    }
}
