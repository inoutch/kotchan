package io.github.inoutch.kotchan.core.module

import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I

fun calcViewport(
    viewportSize: Vector2I,
    screenSize: Vector2I,
    screenType: ScreenType
): RectI {
    val aspectRatio = viewportSize.y / viewportSize.x
    return when (screenType) {
        ScreenType.EXTEND -> RectI(Vector2I.Zero, viewportSize)
        ScreenType.FIX_WIDTH -> RectI(Vector2I.Zero, viewportSize)
        ScreenType.FIX_HEIGHT -> RectI(Vector2I.Zero, viewportSize)
        ScreenType.BORDER -> {
            val screenRatio = screenSize.y / screenSize.x
            return when {
                aspectRatio > screenRatio -> {
                    // top-bottom
                    val border = (viewportSize.y - viewportSize.x * screenRatio) / 2.0f
                    RectI(Vector2I(0.0f, border), Vector2I(viewportSize.x.toFloat(), viewportSize.y - border))
                }
                aspectRatio < screenRatio -> {
                    // right-left
                    val border = (viewportSize.x - viewportSize.y / screenRatio) / 2.0f
                    RectI(Vector2I(border, 0.0f), Vector2I(viewportSize.x - border, viewportSize.y.toFloat()))
                }
                else -> RectI(Vector2I.Zero, viewportSize)
            }
        }
    }
}

fun calcScreenSize(
    viewportSize: Vector2I,
    screenSize: Vector2I,
    screenType: ScreenType
): Vector2I {
    val windowRatio = viewportSize.y.toFloat() / viewportSize.x.toFloat()
    return when (screenType) {
        ScreenType.EXTEND -> screenSize
        ScreenType.FIX_WIDTH -> Vector2I(screenSize.x, (screenSize.x * windowRatio).toInt())
        ScreenType.FIX_HEIGHT -> Vector2I((screenSize.y / windowRatio).toInt(), screenSize.y)
        ScreenType.BORDER -> screenSize
    }
}
