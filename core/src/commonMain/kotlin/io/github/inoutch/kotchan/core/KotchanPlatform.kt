package io.github.inoutch.kotchan.core

expect class KotchanPlatform constructor() {
    suspend fun launch(engine: KotchanEngine, platformConfig: KotchanPlatformConfig? = null)
}
