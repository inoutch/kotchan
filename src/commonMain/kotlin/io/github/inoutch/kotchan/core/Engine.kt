package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.core.math.Vector2I

abstract class Engine {
    abstract val applicationName: String

    abstract val screenType: ScreenType

    abstract val windowSizes: List<Vector2I>

    suspend fun run() {
    }
}
