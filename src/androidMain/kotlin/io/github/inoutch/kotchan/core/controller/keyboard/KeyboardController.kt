package io.github.inoutch.kotchan.core.controller.keyboard

import io.github.inoutch.kotchan.android.KotchanActivity

actual class KeyboardController actual constructor() {
    actual val isOpened: Boolean
        get() = KotchanActivity.keyboard?.isOpened() ?: false

    actual var text: String
        get() = KotchanActivity.keyboard?.text ?: ""
        set(value) {
            KotchanActivity.keyboard?.text = value
        }

    actual fun open(initialText: String, listener: KeyboardListener) {
        KotchanActivity.keyboard?.show(initialText, { listener.onInput(it) }, { listener.onReturn() })
    }

    actual fun close() {
        KotchanActivity.keyboard?.close()
    }
}
