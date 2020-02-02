package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.Vector2I

abstract class KotchanStartupConfig {
    abstract val applicationName: String
    abstract val windowSize: Vector2I
    abstract val screenSize: Vector2I
    open val screenType: ScreenType = ScreenType.EXTEND
    open val useVulkanIfSupported = true
    open val fps: Int = 60

    abstract fun createFirstScene(context: SceneContext): Scene

    open fun onError(error: Error): Boolean = true
}
