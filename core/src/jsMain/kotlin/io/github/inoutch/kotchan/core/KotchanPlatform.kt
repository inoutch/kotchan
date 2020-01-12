package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.platform.WebGLLauncher
import io.github.inoutch.kotchan.core.platform.WebGLPlatformConfig

actual class KotchanPlatform actual constructor() {
    private lateinit var launcher: WebGLLauncher

    actual suspend fun launch(engine: KotchanEngine, platformConfig: KotchanPlatformConfig?) {
        val config = platformConfig as? WebGLPlatformConfig ?: WebGLPlatformConfig()
        launcher = WebGLLauncher(engine, config)
        launcher.startAnimation()
    }
}