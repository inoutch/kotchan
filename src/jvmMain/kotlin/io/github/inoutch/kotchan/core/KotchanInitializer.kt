package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.jvm.GLFWLauncher

actual class KotchanInitializer {
    actual companion object {
        actual fun initialize(config: KotchanEngine.Config) {
            GLFWLauncher(config).run()
        }
    }
}
