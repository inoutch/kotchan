import kmvk.MVKView
import kotlinx.cinterop.ExportObjCClass
import platform.Foundation.NSCoder

@ExportObjCClass
class View : MVKView {
    @OverrideInit
    constructor(coder: NSCoder) : super(coder)
}
