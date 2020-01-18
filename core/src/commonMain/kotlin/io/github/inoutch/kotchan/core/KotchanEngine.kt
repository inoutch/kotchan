package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.view.scene.SceneManager
import io.github.inoutch.kotchan.math.Vector2I

class KotchanEngine(config: KotchanStartupConfig) {
    val startupConfig = config

    var viewportSize: Vector2I = Vector2I.Zero

    lateinit var platform: KotchanPlatform
        private set

    private val sceneManager = SceneManager()

    suspend fun run(platformConfig: KotchanPlatformConfig? = null) {
        try {
            platform = KotchanPlatform(this, platformConfig)
            KotchanGlobalContext().initialize(platform)

            sceneManager.transitScene { startupConfig.createFirstScene(it) }

            platform.launch()
        } catch (e: Error) {
            if (startupConfig.onError(e)) {
                throw e
            }
        }
    }

    suspend fun render(delta: Float) {
        sceneManager.render(delta)
    }

    suspend fun reshape(windowSize: Vector2I, viewportSize: Vector2I) {

    }
}
