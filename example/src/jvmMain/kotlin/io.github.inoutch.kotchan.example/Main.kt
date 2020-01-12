package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanEngine
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        KotchanEngine(AppConfig()).run()
    }
}
