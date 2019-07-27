package io.github.inoutch.kotchan.core.controller.keyboard

import io.github.inoutch.kotchan.ios.KotchanViewContext
import io.github.inoutch.kotchan.ios.view.NativeTextField
import platform.CoreGraphics.CGRectMake
import platform.UIKit.*

actual class KeyboardController {
    private var currentTextField: NativeTextField? = null

    actual val isOpened: Boolean
        get() = currentTextField?.isFirstResponder == true

    actual var text: String
        get() = currentTextField?.text ?: ""
        set(value) {
            currentTextField?.text = value
        }

    actual fun open(initialText: String, listener: KeyboardListener) {
        if (currentTextField != null) {
            close()
        }

        val textField = NativeTextField(CGRectMake(0.0, 0.0, 0.0, 0.0), {
            listener.onInput(it)
        }, {
            listener.onReturn()
        })
        textField.alpha = 0.0
        textField.text = initialText
        KotchanViewContext.viewContext.viewController.view.addSubview(textField)
        textField.becomeFirstResponder()
        currentTextField = textField
    }

    actual fun close() {
        currentTextField?.apply {
            resignFirstResponder()
            removeFromSuperview()
            dispose()
        }
        currentTextField = null
    }
}
