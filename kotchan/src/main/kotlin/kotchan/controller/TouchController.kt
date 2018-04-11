package kotchan.controller

import kotchan.controller.touchable.Touchable

interface TouchController {
    fun add(touchable: Touchable)
    fun touches(): List<Touch>
    fun touchesByOneCycle(index: Int): Touch?
}