package io.github.inoutch.kotchan.core.controller.touch.filter

import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.utility.time.Timer

class QuickTouchFilter(val duration: Long = 300) {
    enum class Type {
        Invalid,
        Preparing,
        TouchedQuickly,
        TouchedSlowly,
    }

    private var time: Long = 0

    fun touch(type: TouchType): Type {
        return when (type) {
            TouchType.None -> Type.Invalid
            TouchType.Began -> {
                time = Timer.milliseconds()
                Type.Preparing
            }
            TouchType.Moved -> Type.Preparing
            TouchType.Ended -> {
                val time = Timer.milliseconds()
                if (time - this.time < duration) {
                    Type.TouchedQuickly
                } else {
                    Type.TouchedSlowly
                }
            }
            TouchType.Cancelled -> Type.Invalid
        }
    }
}
