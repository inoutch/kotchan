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
            // Initialize platform
            platform = KotchanPlatform(this, platformConfig)
            val externalLauncher = (platformConfig as? ExternalPlatformLauncherConfig)?.createLauncher(this)
            val launcher = platform.createLauncher()
            KotchanGlobalContext().initialize(startupConfig, externalLauncher ?: launcher)

            // Initialize kotchan engine
            sceneManager.transitScene { startupConfig.createFirstScene(it) }

            // Launch application
            launcher.startAnimation()
        } catch (e: Error) {
            if (startupConfig.onError(e)) {
                throw e
            }
        }
    }

    suspend fun render(delta: Float) {
        graphic.begin()

        sceneManager.render(delta)

        graphic.end()
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
