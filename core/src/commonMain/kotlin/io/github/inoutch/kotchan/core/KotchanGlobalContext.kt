package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.io.file.File

class KotchanGlobalContext {
    companion object {
        lateinit var file: File
            private set

        lateinit var graphic: Context
            private set

        val config: KotchanConfig = KotchanConfig()
    }

    fun initialize(platform: KotchanPlatform) {
        file = File()
        graphic = platform.graphic
    }
}
