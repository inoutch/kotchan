package io.github.inoutch.kotchan.core

actual class KotchanInstance {
    @kotlin.native.ThreadLocal
    actual companion object {
        actual fun manager() = manager

        private val manager = KotchanInstanceManager()
    }
}