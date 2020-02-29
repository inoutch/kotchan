package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanStartupConfig
import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.Vector2I

class AppConfig : KotchanStartupConfig() {
    override val applicationName: String = "Kotchan Example"
    override val windowSize: Vector2I = Vector2I(750 / 2, 1335 / 2)
    override val screenSize: Vector2I = Vector2I(750, 1300)
    override val screenType: ScreenType = ScreenType.FIX_HEIGHT
    override val useVulkanIfSupported: Boolean = true

    override fun onError(error: Error): Boolean {
        println(error)
        return false
    }

    @ExperimentalStdlibApi
    override fun createFirstScene(context: SceneContext): Scene {
        return SpriteAnimationScene(context)
    }
}
