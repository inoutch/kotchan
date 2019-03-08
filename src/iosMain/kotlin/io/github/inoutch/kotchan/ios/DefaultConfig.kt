package io.github.inoutch.kotchan.ios

import io.github.inoutch.kotchan.core.KotchanEngine

class DefaultConfig {
    @kotlin.native.concurrent.ThreadLocal
    companion object {
        var config: KotchanEngine.Config? = null
    }
}
