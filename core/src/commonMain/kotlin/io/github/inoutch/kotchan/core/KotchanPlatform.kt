package io.github.inoutch.kotchan.core

expect class KotchanPlatform constructor(engine: KotchanEngine, platformConfig: KotchanPlatformConfig?) {
    fun createLauncher(): KotchanPlatformLauncher
}
