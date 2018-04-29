package kotchan.controller.touch

import kotchan.controller.touch.listener.TouchListener

interface TouchController {
    fun add(touchListener: TouchListener, priority: Int = 0)
    fun clearAll()
}