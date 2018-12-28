package io.github.inoutch.kotchan.core

import io.github.inoutch.jvm.JoglLauncher
import com.sun.javafx.application.PlatformImpl

actual class KotchanInitializer {
    actual companion object {
        actual fun initialize(config: KotchanEngine.Config) {
            PlatformImpl.startup { }
            JoglLauncher(config).run()
        }
    }
}