import kotlin.jvm.JvmStatic

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        JoglLauncher("サンプル").apply { run() }
    }
}