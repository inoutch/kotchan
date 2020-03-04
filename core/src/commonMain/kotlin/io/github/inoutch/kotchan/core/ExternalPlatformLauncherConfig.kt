package io.github.inoutch.kotchan.core

interface ExternalPlatformLauncherConfig : KotchanPlatformConfig {
    fun createLauncher(engine: KotchanEngine): KotchanPlatformLauncher
}
