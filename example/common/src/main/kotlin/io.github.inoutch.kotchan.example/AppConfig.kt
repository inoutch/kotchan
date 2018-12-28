import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.core.constant.ScreenType
import io.github.inoutch.kotchan.utility.type.Point
import kotchan.logger.LogLevel

class AppConfig : KotchanEngine.Config() {

    override val appName = "test-jvm"

    override val logLevel = LogLevel.DEBUG

    override val screenSize = Point(640, 1136) / 4

    override val screenType = ScreenType.EXTEND

    override val windowSize = Point(640, 1136)

    override fun initScene() = AppScene()
}