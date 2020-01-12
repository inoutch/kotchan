package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanPlatformConfig
import io.github.inoutch.kotchan.core.KotchanStartupConfig

data class GLFWLauncherConfig(
        val common: KotchanStartupConfig,
        val platform: KotchanPlatformConfig?
)
