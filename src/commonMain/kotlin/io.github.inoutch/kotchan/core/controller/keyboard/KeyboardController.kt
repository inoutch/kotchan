package io.github.inoutch.kotchan.core.controller.keyboard

import kotlin.native.concurrent.ThreadLocal

interface KeyboardListener {
    fun onInput(value: String)
    fun onReturn()
}

expect class KeyboardController() {
    val isOpened: Boolean

    fun open(initialText: String, listener: KeyboardListener)

    fun close()
}

@ThreadLocal
val keyboardController = KeyboardController()
