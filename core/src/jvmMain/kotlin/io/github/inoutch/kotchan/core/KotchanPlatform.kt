package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.platform.GLFWLauncher
import io.github.inoutch.kotchan.core.platform.GLFWLauncherConfig

actual class KotchanPlatform {
    private lateinit var launcher: GLFWLauncher

    actual suspend fun launch(engine: KotchanEngine, platformConfig: KotchanPlatformConfig?) {
        val config = GLFWLauncherConfig(engine.startupConfig, platformConfig)
        launcher = GLFWLauncher(engine, config)
        launcher.startAnimation()
    }
}
