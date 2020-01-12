package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanEngine

suspend fun main() {
    KotchanEngine(ApplicationConfig()).run()
}
