package io.github.inoutch.kotchan.core

interface ExternalPlatformLauncherConfig {
    fun createLauncher(engine: KotchanEngine): KotchanPlatformLauncher
}
