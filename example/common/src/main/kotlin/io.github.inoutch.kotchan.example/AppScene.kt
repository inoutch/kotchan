import io.github.inoutch.kotchan.core.view.Scene
import io.github.inoutch.kotchan.utility.audio.*

class AppScene : Scene() {

    private val bgm: BGM

    init {
        val filepath = file.getResourcePath("sample.mp3")
                ?: throw Error("sample.mp3 is not found")

        bgm = BGM(filepath)
        bgm.loop(-1)
    }

    override fun draw(delta: Float) {}

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun destroyed() {}
}