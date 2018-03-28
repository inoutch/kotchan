import utility.type.Size
import kotlin.jvm.JvmStatic

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        JoglLauncher("サンプル").apply {
            windowSize = Size(640.0f, 1136.0f)
            run()
        }
    }
}