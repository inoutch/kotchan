package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.view.Label
import io.github.inoutch.kotchan.core.graphic.shader.NoColorsShaderProgram
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.ui.button.MockButton
import io.github.inoutch.kotchan.core.graphic.ui.template.Template
import io.github.inoutch.kotchan.core.graphic.ui.template.TemplateAppendType
import io.github.inoutch.kotchan.core.graphic.ui.template.TemplateType
import io.github.inoutch.kotchan.utility.audio.BGM
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError
import io.github.inoutch.kotchan.utility.io.readTextFromResourceWithError
import io.github.inoutch.kotchan.utility.type.Rect
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector4

class BgmScene : Scene() {

    private val camera = KotchanCore.instance.createCamera2D()

    private val bgm: BGM

    private val batch = Batch()

    private val backLabel: Label

    private val backButton: MockButton

    private val playLabel: Label

    private val playButton: MockButton

    private val stopLabel: Label

    private val stopButton: MockButton

    private val noColorsShaderProgram = NoColorsShaderProgram()

    private val buttonShaderProgram = SimpleShaderProgram()

    init {
        val bmFont = BMFont.parse(file.readTextFromResourceWithError("font/sample.fnt"))

        bgm = BGM(file.getResourcePathWithError("sample.mp3"))

        val template = Template(Rect(Vector2.Zero, screenSize.toVector2()))

        backLabel = Label(bmFont, file.getResourcePathWithError("font"), "Back")
        batch.addViewBase(backLabel, noColorsShaderProgram)
        template.add(TemplateType.MiddleCenter, TemplateAppendType.Row, 24.0f, backLabel)

        stopLabel = Label(bmFont, file.getResourcePathWithError("font"), "Stop")
        batch.addViewBase(stopLabel, noColorsShaderProgram)
        template.add(TemplateType.MiddleCenter, TemplateAppendType.Row, 24.0f, stopLabel)

        playLabel = Label(bmFont, file.getResourcePathWithError("font"), "Play")
        batch.addViewBase(playLabel, noColorsShaderProgram)
        template.add(TemplateType.MiddleCenter, TemplateAppendType.Row, 24.0f, playLabel)

        template.updatePositions()

        backButton = MockButton(camera, Vector2(240.0f, 40.0f)) {
            bgm.stop()
            bgm.destroy()
            KotchanCore.instance.runScene { AppScene() }
        }
        backButton.position = backLabel.position
        backButton.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
        backButton.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
        touchController.add(backButton.touchListener, 0)
        batch.add(backButton, buttonShaderProgram)

        stopButton = MockButton(camera, Vector2(240.0f, 40.0f)) {
            bgm.stop()
        }
        stopButton.position = stopLabel.position
        stopButton.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
        stopButton.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
        touchController.add(stopButton.touchListener, 0)
        batch.add(stopButton, buttonShaderProgram)

        playButton = MockButton(camera, Vector2(240.0f, 40.0f)) {
            bgm.loop(-1)
        }
        playButton.position = playLabel.position
        playButton.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
        playButton.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
        touchController.add(playButton.touchListener, 0)
        batch.add(playButton, buttonShaderProgram)
    }

    override fun draw(delta: Float) {
        gl.clearColor(0.2f, 0.2f, 0.2f, 1.0f)

        batch.draw(delta, camera)
    }

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun destroyed() {
        backLabel.destroy()
        backButton.destroy()
    }
}
