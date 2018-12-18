package io.github.inoutch.kotchan.core

expect class KotchanInitializer {
    companion object {
        fun initialize(config: KotchanEngine.Config)
    }
}