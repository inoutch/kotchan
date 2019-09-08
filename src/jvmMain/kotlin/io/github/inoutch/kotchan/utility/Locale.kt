package io.github.inoutch.kotchan.utility

import java.util.Locale

actual class Locale {
    actual companion object {
        actual val language: String
            get() = Locale.getDefault().language
    }
}
