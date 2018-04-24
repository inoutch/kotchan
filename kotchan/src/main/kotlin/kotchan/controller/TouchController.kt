package kotchan.controller

import kotchan.controller.touchable.Touchable

interface TouchController {
    fun add(touchable: Touchable, priority: Int = 0)
    fun clearAll()
}