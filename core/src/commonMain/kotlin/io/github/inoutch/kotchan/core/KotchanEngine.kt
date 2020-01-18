package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.view.scene.SceneManager
import io.github.inoutch.kotchan.math.Vector2I

class KotchanEngine(config: KotchanStartupConfig) {
    val startupConfig = config

    var viewportSize: Vector2I = Vector2I.Zero

    private lateinit var platform: KotchanPlatform

    private val sceneManager = SceneManager()

    suspend fun run(platformConfig: KotchanPlatformConfig? = null) {
        try {
            initialize()

            platform = KotchanPlatform()
            platform.launch(this, platformConfig)
            initialize()
        } catch (e: Error) {
            startupConfig.onError(e)
        }
    }

    suspend fun render(delta: Float) {
        if (!sceneManager.isInitialized) {
            sceneManager.init()
        }
    }

    suspend fun reshape(windowSize: Vector2I, viewportSize: Vector2I) {

    }

    private fun initialize() {
        KotchanGlobalContext().initialize()
    }
}
