package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.controller.touch.listener.GridTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.ScrollTouchListener
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.polygon.TextLabel
import io.github.inoutch.kotchan.core.graphic.polygon.tile.TileLayer
import io.github.inoutch.kotchan.core.graphic.polygon.tile.TileMap
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.template.Template
import io.github.inoutch.kotchan.core.graphic.template.TemplateAppendType
import io.github.inoutch.kotchan.core.graphic.template.TemplateType
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.core.graphic.ui.button.ColorButton
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.time.Timer
import io.github.inoutch.kotchan.utility.type.*
import kotlin.random.Random

class TileMapScene : Scene() {

    private val shaderProgram = SimpleShaderProgram()

    private val camera = KotchanCore.instance.createCamera2D()

    private val uiCamera = KotchanCore.instance.createCamera2D()

    private val buttonMaterial: Material

    private val tileMaterial: Material

    private val titleTextLabel: TextLabel

    private val batch = disposer.add(Batch())

    private val uiBatch = disposer.add(Batch())

    private var colorCircle = 0.0f

    private val tileMap: TileMap

    init {
        val bmFont = disposer.add(BMFont.loadFromResource(
                "font/sample.fnt", "font",
                Material.Config(shaderProgram).also { it.depthTest = false }))
        titleTextLabel = TextLabel(bmFont, "Tile Map Example")

        buttonMaterial = disposer.add(Material(Material.Config(shaderProgram, Texture.emptyTexture()).also {
            it.depthTest = false
        }))

        tileMaterial = disposer.add(Material(Material.Config(shaderProgram, Texture.loadFromResource("tiles/sample.png"))))
        tileMaterial.textureAutoRelease = true

        val transitions = listOf("Back" to {
            instance.runScene { AppScene() }
        })
        val buttons = transitions.map {
            val button = ColorButton(buttonMaterial, uiCamera, Vector2(250, 32), it.second)
            button.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
            button.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
            touchController.add(button.touchListener)

            val label = TextLabel(bmFont, it.first)
            label.position = Vector3(0.0f, 0.0f, -0.1f)
            button.addChild(label)
            button
        }

        uiBatch.add(titleTextLabel, *buttons.toTypedArray())

        Template(Rect(
                Vector2(0.0f, screenSize.y.toFloat() - 300.0f),
                Vector2(screenSize.x.toFloat(), 300.0f))).apply {
            add(TemplateType.MiddleCenter, TemplateAppendType.Row, 12.0f, 0.0f,
                    listOf(titleTextLabel, *buttons.toTypedArray()).reversed())
            updatePositions()
        }

        val random = Random(Timer.milliseconds())
        val tileMapSize = Point(10, 10)
        tileMap = TileMap(TileMap.Config(
                tileMaterial,
                Point(32, 32),
                Point(16, 16),
                tileMapSize,
                TileLayer.Config(List(3) { Array2D(tileMapSize) { random.nextInt(0, 4) } })))
        batch.add(tileMap)

        touchController.add(GridTouchListener(camera, tileMap.config.tileSize, { point: Point, touchType: TouchType ->
            if (touchType == TouchType.Began) {
                tileMap.layer(0)?.setGraphicId(point, random.nextInt(0, 4))
            }
            true
        }))

        ScrollTouchListener(uiCamera) {
            camera.position -= Vector3(it.x, it.y, 0.0f)
            camera.update()
        }.also {
            it.accelerationEnable = true
            touchController.add(it)
        }
    }

    override fun draw(delta: Float) {
        colorCircle += delta * 0.2f
        if (colorCircle > 1.0f) colorCircle -= 1.0f

        val color = Color.hsv2rgb(colorCircle, 1.0f, 1.0f)
        titleTextLabel.color = Vector4(color, 1.0f)

        KotchanCore.instance.graphicsApi.clearColor(Vector4(0.2f, 0.2f, 0.2f, 1.0f))
        KotchanCore.instance.graphicsApi.clearDepth(1.0f)
        KotchanCore.instance.graphicsApi.setViewport(KotchanCore.instance.viewport)

        batch.draw(delta, camera)
        uiBatch.draw(delta, uiCamera)
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}
}
