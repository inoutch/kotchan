package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.core.module.calcScreenSize
import io.github.inoutch.kotchan.core.module.calcViewport
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I

class KotchanConfig {
    // Only applies if the screen size is not fixed
    var viewport: RectI = RectI(Vector2I.Zero, Vector2I.Zero)
        private set

    var windowSize = Vector2I.Zero
        private set

    var viewportSize = Vector2I.Zero
        private set

    var screenSize = Vector2I.Zero
        private set

    fun updateSize(
        windowSize: Vector2I,
        viewportSize: Vector2I,
        screenSize: Vector2I,
        screenType: ScreenType
    ) {
        this.windowSize = windowSize
        this.viewportSize = viewportSize
        this.viewport = calcViewport(viewportSize, screenSize, screenType)
        this.screenSize = calcScreenSize(viewportSize, screenSize, screenType)
    }
}
