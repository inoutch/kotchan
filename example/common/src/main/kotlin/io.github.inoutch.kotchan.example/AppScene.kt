package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.view.Scene
import io.github.inoutch.kotchan.core.view.batch.Batch
import io.github.inoutch.kotchan.core.view.drawable.Label
import io.github.inoutch.kotchan.core.view.shader.NoColorsShaderProgram
import io.github.inoutch.kotchan.core.view.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.view.ui.MockButton
import io.github.inoutch.kotchan.core.view.ui.template.Template
import io.github.inoutch.kotchan.core.view.ui.template.TemplateAppendType
import io.github.inoutch.kotchan.core.view.ui.template.TemplateType
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError
import io.github.inoutch.kotchan.utility.io.readTextFromResourceWithError
import io.github.inoutch.kotchan.utility.type.*

class AppScene : Scene() {
    private var colorCircle = 0.0f

    private val camera = KotchanCore.instance.createCamera2D()
    private val noColorsShaderProgram = NoColorsShaderProgram()
    private val buttonShaderProgram = SimpleShaderProgram()
    private val labelShaderProgram = SimpleShaderProgram()
    private val batch = Batch()

    private val exampleLabel: Label

    private val bgmLabel: Label
    private val bgmButton: MockButton

    private val tileLabel: Label
    private val tileButton: MockButton

    init {
        val bmFont = BMFont.parse(file.readTextFromResourceWithError("font/sample.fnt"))

        val template = Template(Rect(Vector2.Zero, screenSize.toVector2()))

        exampleLabel = Label(bmFont, file.getResourcePathWithError("font"), "Kotchan Examples")
        batch.add(exampleLabel, labelShaderProgram)

        bgmLabel = Label(bmFont, file.getResourcePathWithError("font"), "Sound Example")
        batch.add(bgmLabel, noColorsShaderProgram)

        tileLabel = Label(bmFont, file.getResourcePathWithError("font"), "Tile Map Example")
        batch.add(tileLabel, noColorsShaderProgram)

        template.add(TemplateType.MiddleCenter, TemplateAppendType.Row, 24.0f, tileLabel)
        template.add(TemplateType.MiddleCenter, TemplateAppendType.Row, 24.0f, bgmLabel)
        template.add(TemplateType.MiddleCenter, TemplateAppendType.Row, 24.0f, exampleLabel)
        template.updatePositions()

        bgmButton = MockButton(camera, Vector2(240.0f, 40.0f)) {
            KotchanCore.instance.runScene { BgmScene() }
        }
        bgmButton.position = bgmLabel.position
        bgmButton.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
        bgmButton.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
        touchController.add(bgmButton.touchListener, 0)
        batch.add(bgmButton, buttonShaderProgram)

        tileButton = MockButton(camera, Vector2(240.0f, 40.0f)) {
            KotchanCore.instance.runScene { TileMapScene() }
        }
        tileButton.position = tileLabel.position
        tileButton.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
        tileButton.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
        touchController.add(tileButton.touchListener, 0)
        batch.add(tileButton, buttonShaderProgram)
    }

    override fun draw(delta: Float) {
        colorCircle += delta * 0.2f
        if (colorCircle > 1.0f) colorCircle -= 1.0f

        val color = Color.hsv2rgb(colorCircle, 1.0f, 1.0f)
        exampleLabel.color = Vector4(color, 1.0f)

        gl.clearColor(0.2f, 0.2f, 0.2f, 1.0f)
        gl.enableBlend()
        gl.activeTexture(0)
        batch.draw(delta, camera)
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun destroyed() {
        batch.destroy()

        noColorsShaderProgram.destroy()
        buttonShaderProgram.destroy()
        labelShaderProgram.destroy()

        exampleLabel.destroy()

        tileLabel.destroy()
        tileButton.destroy()
        bgmLabel.destroy()
        bgmButton.destroy()
    }
}