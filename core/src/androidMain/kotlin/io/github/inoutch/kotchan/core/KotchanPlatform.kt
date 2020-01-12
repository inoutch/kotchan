package io.github.inoutch.kotchan.core

actual class KotchanPlatform actual constructor() {
    actual suspend fun launch(engine: KotchanEngine, platformConfig: KotchanPlatformConfig?) {
    }
}
