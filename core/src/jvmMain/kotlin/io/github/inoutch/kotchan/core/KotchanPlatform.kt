package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.platform.GLFWLauncher
import io.github.inoutch.kotchan.core.platform.GLFWLauncherConfig

actual class KotchanPlatform actual constructor(
        private val engine: KotchanEngine,
        private val platformConfig: KotchanPlatformConfig?
) {
    actual fun createLauncher(): KotchanPlatformLauncher {
        return GLFWLauncher(
                engine,
                GLFWLauncherConfig(engine.startupConfig, platformConfig)
        )
    }
}
