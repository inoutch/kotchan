package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.controller.touch.listener.ScrollTouchListener
import io.github.inoutch.kotchan.core.view.Scene
import io.github.inoutch.kotchan.core.view.batch.Batch
import io.github.inoutch.kotchan.core.view.drawable.Label
import io.github.inoutch.kotchan.core.view.map.TileLayerInfo
import io.github.inoutch.kotchan.core.view.map.TileMap
import io.github.inoutch.kotchan.core.view.map.TileMapInfo
import io.github.inoutch.kotchan.core.view.shader.NoColorsShaderProgram
import io.github.inoutch.kotchan.core.view.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.view.ui.button.MockButton
import io.github.inoutch.kotchan.core.view.ui.template.Template
import io.github.inoutch.kotchan.core.view.ui.template.TemplateAppendType
import io.github.inoutch.kotchan.core.view.ui.template.TemplateType
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError
import io.github.inoutch.kotchan.utility.io.readTextFromResourceWithError
import io.github.inoutch.kotchan.utility.type.*
import kotlin.random.Random

class TileMapScene : Scene() {
    private val random = Random(0)

    private val scrollCamera = KotchanCore.instance.createCamera2D()

    private val uiCamera = KotchanCore.instance.createCamera2D()

    private val noColorsShaderProgram = NoColorsShaderProgram()

    private val buttonShaderProgram = SimpleShaderProgram()

    private val batch = Batch()

    private val backLabel: Label

    private val backButton: MockButton

    private val tiles = List(100) { List(100) { random.nextInt() % 4 } }

    private val texture = textureManager.getFromResource("tiles/sample.png")

    private val tileMap = TileMap(TileMapInfo(Point(16, 16), texture, List(1) { TileLayerInfo(tiles) }))

    init {
        ScrollTouchListener(scrollCamera) {
            scrollCamera.position -= Vector3(it, 0.0f)

            scrollCamera.position = Vector3(if (scrollCamera.position.x < 0) {
                0.0f
            } else if (scrollCamera.position.x > 16 * 100 - screenSize.x) {
                16.0f * 100.0f - screenSize.x
            } else scrollCamera.position.x, scrollCamera.position.y, scrollCamera.position.z)

            scrollCamera.position = Vector3(scrollCamera.position.x, if (scrollCamera.position.y < 0) {
                0.0f
            } else if (scrollCamera.position.y > 16 * 100 - screenSize.y) {
                16.0f * 100.0f - screenSize.y
            } else {
                scrollCamera.position.y
            }, scrollCamera.position.z)

            scrollCamera.update()
        }.also {
            touchController.add(it)
            it.accelerationEnable = true
        }

        val bmFont = BMFont.parse(file.readTextFromResourceWithError("font/sample.fnt"))

        val template = Template(Rect(Vector2.Zero, screenSize.toVector2()))

        backLabel = Label(bmFont, file.getResourcePathWithError("font"), "Back")
        batch.add(backLabel, noColorsShaderProgram)
        template.add(TemplateType.MiddleCenter, TemplateAppendType.Row, 24.0f, backLabel)
        template.updatePositions()

        backButton = MockButton(uiCamera, Vector2(240.0f, 40.0f)) {
            KotchanCore.instance.runScene { AppScene() }
        }
        backButton.position = backLabel.position
        backButton.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
        backButton.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
        touchController.add(backButton.touchListener, 0)
        batch.add(backButton, buttonShaderProgram)
    }

    override fun draw(delta: Float) {
        gl.clearColor(0.2f, 0.2f, 0.2f, 1.0f)

        tileMap.draw(delta, scrollCamera)
        batch.draw(delta, uiCamera)
    }

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun destroyed() {
        tileMap.destroy()
        batch.destroy()
        backButton.destroy()
        backLabel.destroy()

        noColorsShaderProgram.destroy()
        buttonShaderProgram.destroy()

        textureManager.clearAll()
    }
}