package kotchan.logger

import application.AppConfig

class Logger(private val logLevel: LogLevel) {
    private fun log(level: LogLevel, message: Any) {
        println(level.name + "\t" + message)
    }

    fun info(message: Any) {
        if (logLevel.value <= LogLevel.INFO.value) {
            log(LogLevel.INFO, message)
        }
    }

    fun debug(message: Any) {
        if (logLevel.value <= LogLevel.DEBUG.value) {
            log(LogLevel.DEBUG, message)
        }
    }

    fun warn(message: Any) {
        if (logLevel.value <= LogLevel.WARN.value) {
            log(LogLevel.WARN, message)
        }
    }

    fun error(message: Any) {
        if (logLevel.value <= LogLevel.ERROR.value) {
            log(LogLevel.ERROR, message)
        }
    }

    fun error(vararg messages: Any) {
        error(listOf(messages))
    }
}

val logger = Logger(AppConfig.LOG_LEVEL)