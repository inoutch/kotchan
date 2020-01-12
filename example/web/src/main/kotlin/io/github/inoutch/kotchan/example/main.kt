package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanEngine
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.browser.window

fun main() {
    window.onload = {
        GlobalScope.launch {
            KotchanEngine(AppConfig()).run()
        }
    }
}
