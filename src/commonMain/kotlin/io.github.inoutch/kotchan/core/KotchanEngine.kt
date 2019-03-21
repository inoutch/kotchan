package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.logger.LoggerFactory
import io.github.inoutch.kotchan.utility.type.Point
import kotchan.logger.LogLevel

class KotchanEngine(private val config: Config) {

    abstract class Config {

        abstract val appName: String

        abstract val screenType: ScreenType

        abstract val windowSize: Point

        abstract val screenSize: Point

        abstract val logLevel: LogLevel

        abstract fun initScene(): Scene

        open val loggerFactory: LoggerFactory? = null
    }

    fun run() {
        KotchanInitializer.initialize(config)
    }
}
