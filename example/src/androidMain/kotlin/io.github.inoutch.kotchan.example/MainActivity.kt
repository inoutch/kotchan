package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanStartupConfig
import io.github.inoutch.kotchan.core.platform.KotchanActivity

class MainActivity : KotchanActivity() {
    override val startupConfig: KotchanStartupConfig = AppConfig()
}
