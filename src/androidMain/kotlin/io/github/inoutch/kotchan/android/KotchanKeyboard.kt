package io.github.inoutch.kotchan.android

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KotchanKeyboard(
    private val editText: EditText,
    private val inputMethodManager: InputMethodManager
) {
    private var current: TextWatcher? = null

    fun isOpened(): Boolean {
        return current != null
    }

    fun show(initialText: String, onInput: (text: String) -> Unit, onReturn: () -> Unit) {
        val editText = this.editText
        current?.let { editText.removeTextChangedListener(it) }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {}
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                onInput(p0.toString())
            }
        }
        editText.addTextChangedListener(watcher)
        editText.setText(initialText)
        editText.setSingleLine()
        editText.setSelection(initialText.length)
        editText.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        editText.setOnEditorActionListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENDCALL) {
                onReturn()
                true
            } else {
                false
            }
        }
        inputMethodManager.showSoftInput(editText, 0)
        current = watcher
    }

    fun close() {
        val editText = this.editText
        editText.setOnKeyListener(null)
        current?.let { editText.removeTextChangedListener(it) }
        current = null

        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    var text: String
        get() = editText.text?.toString() ?: ""
        set(value) {
            GlobalScope.launch(Dispatchers.Main) {
                editText.setText(value)
                editText.setSelection(value.length)
            }
        }
}
