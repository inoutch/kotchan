import kotlinx.cinterop.ExportObjCClass
import mvkview.MVKView
import platform.Foundation.NSCoder

@ExportObjCClass
class View : MVKView {
    @OverrideInit
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(coder: NSCoder) : super(coder)
}
