package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context

expect class KotchanPlatform constructor(engine: KotchanEngine, platformConfig: KotchanPlatformConfig?) {
    val graphic: Context

    suspend fun launch()
}
