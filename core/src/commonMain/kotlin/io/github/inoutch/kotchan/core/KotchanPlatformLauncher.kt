package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.compatible.context.Context

interface KotchanPlatformLauncher {
    fun getGraphics(): Context
    suspend fun startAnimation()
}
