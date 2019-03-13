package io.github.inoutch.kotchan.core.logger

import kotchan.logger.LogLevel

open class Logger {

    private lateinit var logLevel: LogLevel

    open fun init(logLevel: LogLevel) {
        this.logLevel = logLevel
    }

    open fun info(message: Any) {
        if (logLevel.value <= LogLevel.INFO.value) {
            log(LogLevel.INFO, message)
        }
    }

    open fun debug(message: Any) {
        if (logLevel.value <= LogLevel.DEBUG.value) {
            log(LogLevel.DEBUG, message)
        }
    }

    open fun warn(message: Any) {
        if (logLevel.value <= LogLevel.WARN.value) {
            log(LogLevel.WARN, message)
        }
    }

    open fun error(message: Any) {
        if (logLevel.value <= LogLevel.ERROR.value) {
            log(LogLevel.ERROR, message)
        }
    }

    open fun error(vararg messages: Any) {
        error(listOf(messages))
    }

    private fun log(level: LogLevel, message: Any) {
        println(level.name + "\t" + message)
    }
}
