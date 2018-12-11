import application.AppConfig
import kotlin.jvm.JvmStatic

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        JoglLauncher(AppConfig.APP_NAME).apply { run() }
    }
}
