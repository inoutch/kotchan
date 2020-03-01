package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.platform.WebGLLauncher
import io.github.inoutch.kotchan.core.platform.WebGLPlatformConfig

actual class KotchanPlatform actual constructor(
        private val engine: KotchanEngine,
        private val platformConfig: KotchanPlatformConfig?
) {

    actual fun createLauncher(): KotchanPlatformLauncher {
        val config = platformConfig as? WebGLPlatformConfig ?: WebGLPlatformConfig()
        return WebGLLauncher(engine, config)
    }
}
