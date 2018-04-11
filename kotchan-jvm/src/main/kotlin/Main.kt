import utility.type.Vector2
import kotlin.jvm.JvmStatic

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        JoglLauncher("サンプル").apply {
            windowSize = Vector2(640.0f, 1136.0f) / 2.0f
            run()
        }
    }
}