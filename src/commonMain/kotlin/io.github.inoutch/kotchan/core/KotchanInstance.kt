package io.github.inoutch.kotchan.core

expect class KotchanInstance {
    companion object {
        fun manager(): KotchanInstanceManager
    }
}