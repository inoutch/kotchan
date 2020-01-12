package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanEngineConfig
import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.math.Vector2I

class ApplicationConfig : KotchanEngineConfig() {
    override val applicationName: String = "Kotchan example"
    override val screenType: ScreenType = ScreenType.BORDER
    override val windowSizes: List<Vector2I> = listOf(Vector2I(300, 600))
}