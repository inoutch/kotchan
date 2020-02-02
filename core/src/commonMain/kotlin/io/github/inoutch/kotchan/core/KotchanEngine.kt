package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.view.scene.SceneManager
import io.github.inoutch.kotchan.math.Vector2I

class KotchanEngine(config: KotchanStartupConfig) {
    val startupConfig = config

    lateinit var platform: KotchanPlatform
        private set

    private val sceneManager = SceneManager()

    suspend fun run(platformConfig: KotchanPlatformConfig? = null) {
        try {
            platform = KotchanPlatform(this, platformConfig)
            KotchanGlobalContext().initialize(startupConfig, platform)

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

    fun resize(windowSize: Vector2I, viewportSize: Vector2I) {
        graphic.resize(windowSize)
        initSize(windowSize, viewportSize)
    }

    fun initSize(windowSize: Vector2I, viewportSize: Vector2I) {
        config.updateSize(
                windowSize,
                viewportSize,
                startupConfig.screenSize,
                startupConfig.screenType
        )
    }
}
