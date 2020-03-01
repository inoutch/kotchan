package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.vk.VKContext
import io.github.inoutch.kotchan.core.io.file.File
import kotlin.native.concurrent.ThreadLocal

class KotchanGlobalContext {
    @ThreadLocal
    companion object {
        lateinit var file: File
            private set

        lateinit var graphic: Context
            private set

        lateinit var startupConfig: KotchanStartupConfig
            private set

        var config: KotchanConfig = KotchanConfig()
            private set

        var useVulkan: Boolean = false
            private set
    }

    fun initialize(
        startupConfig: KotchanStartupConfig,
        launcher: KotchanPlatformLauncher
    ) {
        KotchanGlobalContext.startupConfig = startupConfig
        file = File()
        graphic = launcher.getGraphics()
        useVulkan = graphic is VKContext
    }
}
