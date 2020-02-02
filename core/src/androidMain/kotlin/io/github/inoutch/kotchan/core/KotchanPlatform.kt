package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.gl.GLContext

actual class KotchanPlatform actual constructor(
    engine: KotchanEngine,
    platformConfig: KotchanPlatformConfig?
) {
    actual val graphic: Context = GLContext()

    actual suspend fun launch() {
    }
}
