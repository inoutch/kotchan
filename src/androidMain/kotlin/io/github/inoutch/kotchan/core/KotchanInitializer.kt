package io.github.inoutch.kotchan.core

actual class KotchanInitializer {
    actual companion object {
        var defaultConfig: KotchanEngine.Config? = null

        actual fun initialize(config: KotchanEngine.Config) {
            defaultConfig = config
        }
    }
}
