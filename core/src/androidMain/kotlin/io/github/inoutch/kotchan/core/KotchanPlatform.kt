package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.gl.GLContext

actual class KotchanPlatform actual constructor(
    engine: KotchanEngine,
    platformConfig: KotchanPlatformConfig?
) {
    actual fun createLauncher(): KotchanPlatformLauncher {
        return object : KotchanPlatformLauncher {
            override fun getGraphics(): Context {
                return GLContext()
            }

            override suspend fun startAnimation() {
                // Skip
            }
        }
    }
}
