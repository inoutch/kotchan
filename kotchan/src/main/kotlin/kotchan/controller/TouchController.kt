package kotchan.controller

import interop.graphic.GLCamera
import kotchan.controller.touchable.Touchable

interface TouchController {
    fun camera(camera: GLCamera)
    fun add(touchable: Touchable)
    fun touches(): List<Touch>
    fun touchesByOneCycle(index: Int): Touch?
}