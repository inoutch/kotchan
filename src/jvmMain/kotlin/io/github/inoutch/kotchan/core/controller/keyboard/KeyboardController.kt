package io.github.inoutch.kotchan.core.controller.keyboard

private class KeyboardContext(
    initialText: String,
    val listener: KeyboardListener
) {
    var text: String = initialText

    fun input(char: String) {
        text += char
        listener.onInput(text)
    }

    fun delete() {
        if (text.isBlank()) {
            return
        }
        text = text.substring(0, text.length - 1)
        listener.onInput(text)
    }

    fun enter() {
        listener.onReturn()
    }
}

actual class KeyboardController actual constructor() {
    actual val isOpened: Boolean
        get() = currentContext != null

    actual var text: String
        get() = currentContext?.text ?: ""
        set(value) {
            currentContext?.text = value
        }

    private var currentContext: KeyboardContext? = null

    actual fun open(initialText: String, listener: KeyboardListener) {
        currentContext = KeyboardContext(initialText, listener)
    }

    actual fun close() {
        currentContext = null
    }

    fun input(char: String) {
        currentContext?.input(char)
    }

    fun delete() {
        currentContext?.delete()
    }

    fun enter() {
        // Ignore for Japanese
//        currentContext?.enter()
    }
}
