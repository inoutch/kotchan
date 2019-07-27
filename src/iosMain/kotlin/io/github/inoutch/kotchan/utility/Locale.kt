package io.github.inoutch.kotchan.utility

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

actual class Locale {
    actual companion object {
        actual val language: String
            get() {
                val nativeLanguage = NSLocale.preferredLanguages.first() as String
                return nativeLanguage.split("-").first()
            }
    }
}
