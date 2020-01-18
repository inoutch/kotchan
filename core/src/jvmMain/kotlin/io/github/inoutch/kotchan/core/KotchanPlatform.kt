package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.platform.GLFWLauncher
import io.github.inoutch.kotchan.core.platform.GLFWLauncherConfig

actual class KotchanPlatform actual constructor(engine: KotchanEngine, platformConfig: KotchanPlatformConfig?) {
    private val launcher: GLFWLauncher = GLFWLauncher(
            engine,
            GLFWLauncherConfig(engine.startupConfig, platformConfig)
    )

    actual val graphic = launcher.context

    actual suspend fun launch() {
        launcher.startAnimation()
    }
}
