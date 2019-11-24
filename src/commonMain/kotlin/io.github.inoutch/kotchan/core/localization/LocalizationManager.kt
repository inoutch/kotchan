package io.github.inoutch.kotchan.core.localization

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.core.KotchanCore.Companion.logger
import io.github.inoutch.kotchan.utility.Locale
import io.github.inoutch.kotchan.utility.data.json.Json
import io.github.inoutch.kotchan.utility.io.readTextFromResource
import io.github.inoutch.kotchan.utility.io.readTextFromResourceWithError
import io.github.inoutch.kotchan.utility.path.Path
import kotlin.native.concurrent.ThreadLocal

class LocalizationManager {
    private val defaultLocalization = mutableMapOf<String, String>()

    private val localization = mutableMapOf<String, String>()

    @ThreadLocal
    companion object {
        val localizationManager = LocalizationManager()
    }

    fun load(dir: String, default: String = "en") {
        val language = Locale.language
        val defaultJson = core.file.readTextFromResourceWithError(Path.resolve(dir, "$default.json"))
        val defaultLocalization = Json.parse(defaultJson)
                ?: throw IllegalStateException("Invalid json format")

        if (!defaultLocalization.isMap()) {
            throw IllegalStateException("Json is not map")
        }

        defaultLocalization.toMap().forEach {
            val string = if (it.value.isText()) it.value.toText() else null
            if (string != null) {
                this.defaultLocalization[it.key] = string
            }
        }

        if (language == default) {
            return
        }
        val json = core.file.readTextFromResource(Path.resolve(dir, "$language.json"))
        if (json == null) {
            logger.warn("Unexpected language of $language")
            return
        }

        val localization = Json.parse(json)
        if (localization == null) {
            logger.error("Invalid format of $language.json")
            return
        }

        localization.toMap().forEach {
            val string = if (it.value.isText()) it.value.toText() else null
            if (string != null) {
                this.defaultLocalization[it.key] = string
            }
        }
    }

    operator fun get(key: String) = localization[key] ?: defaultLocalization[key] ?: key
}
