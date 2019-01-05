package io.github.inoutch.kotchan.core.controller.touch

import io.github.inoutch.kotchan.core.controller.touch.listener.TouchListener

interface TouchController {

    fun add(touchListener: TouchListener, priority: Int = 0)

    fun remove(touchListener: TouchListener)

    fun clearAll()
}