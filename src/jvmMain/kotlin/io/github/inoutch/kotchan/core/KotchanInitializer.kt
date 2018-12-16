package io.github.inoutch.kotchan.core

import io.github.inoutch.jvm.JoglLauncher

actual class KotchanInitializer {
    actual companion object {
        actual fun initialize(config: KotchanEngine.Config) {
            JoglLauncher(config).run()
        }
    }
}