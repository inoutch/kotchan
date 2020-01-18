package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.platform.WebGLLauncher
import io.github.inoutch.kotchan.core.platform.WebGLPlatformConfig

actual class KotchanPlatform actual constructor(engine: KotchanEngine, platformConfig: KotchanPlatformConfig?) {

    private val launcher: WebGLLauncher

    actual val graphic: Context

    init {
        val config = platformConfig as? WebGLPlatformConfig ?: WebGLPlatformConfig()
        launcher = WebGLLauncher(engine, config)
        graphic = launcher.context
    }

    actual suspend fun launch() {
        launcher.startAnimation()
    }
}