package io.github.inoutch.kotchan.ios.view

import io.github.inoutch.kotchan.ios.KotchanViewContext.Companion.viewContext
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExportObjCClass
import platform.CoreGraphics.CGRect
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.UIKit.UITextFieldTextDidChangeNotification

@ExportObjCClass
class NativeTextField(
        rect: CValue<CGRect>,
        val onChange: (text: String) -> Unit,
        private val onInputReturn: (text: String) -> Unit) : UITextField(rect), UITextFieldDelegateProtocol {
    companion object {
        fun textFieldDidChange(notification: NSNotification) {
            val native = notification.`object` as NativeTextField
            native.onChange(native.text ?: "")
        }
    }

    init {
        NSNotificationCenter.defaultCenter.addObserver(
                viewContext.viewController,
                NSSelectorFromString("textFieldDidChange:"),
                UITextFieldTextDidChangeNotification, this)
        delegate = this
    }

    override fun textFieldShouldReturn(textField: UITextField): Boolean {
        onInputReturn(textField.text ?: "")
        return true
    }

    fun dispose() {
        NSNotificationCenter.defaultCenter.removeObserver(viewContext.viewController)
    }
}
