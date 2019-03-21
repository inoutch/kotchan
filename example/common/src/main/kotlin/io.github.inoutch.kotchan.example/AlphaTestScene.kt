package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.polygon.SpriteAtlas
import io.github.inoutch.kotchan.core.graphic.polygon.TextLabel
import io.github.inoutch.kotchan.core.graphic.shader.AlphaTestSimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.template.Template
import io.github.inoutch.kotchan.core.graphic.template.TemplateAppendType
import io.github.inoutch.kotchan.core.graphic.template.TemplateType
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.core.graphic.ui.button.ColorButton
import io.github.inoutch.kotchan.core.tool.TexturePacker
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.type.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class AlphaTestScene : Scene() {

    private val shaderProgram = AlphaTestSimpleShaderProgram()

    private val uiShaderProgram = SimpleShaderProgram()

    private val camera = KotchanCore.instance.createCamera2D()

    private val uiCamera = KotchanCore.instance.createCamera2D()

    private val buttonMaterial: Material

    private val titleTextLabel: TextLabel

    private val batch = disposer.add(Batch())

    private val uiBatch = disposer.add(Batch())

    private val sprite1: SpriteAtlas

    private val sprite2: SpriteAtlas

    private var colorCircle = 0.0f

    private val centerPosition = Vector3(Vector2(screenSize.x / 2.0f, screenSize.y / 2.0f + 60.0f), 0.0f)

    init {
        val bmFont = disposer.add(BMFont.loadFromResource(
                "font/sample.fnt", "font",
                Material.Config(uiShaderProgram).also { it.depthTest = false }))
        titleTextLabel = TextLabel(bmFont, "Alpha Test Example")

        buttonMaterial = disposer.add(Material(Material.Config(uiShaderProgram, Texture.emptyTexture()).also {
            it.depthTest = false
        }))

        val transitions = listOf("Back" to {
            KotchanCore.instance.runScene { AppScene() }
        })
        val buttons = transitions.map {
            val button = ColorButton(buttonMaterial, uiCamera, Vector2(250, 32), it.second)
            button.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
            button.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
            touchController.add(button.touchListener)

            val label = TextLabel(bmFont, it.first)
            label.position = Vector3(button.size / 2.0f, -0.1f)
            button.addChild(label)
            button
        }

        uiBatch.add(titleTextLabel, *buttons.toTypedArray())

        Template(Rect(Vector2.Zero, Vector2(screenSize.x.toFloat(), 200.0f))).apply {
            add(TemplateType.MiddleCenter, TemplateAppendType.Row, 12.0f, 0.0f,
                    listOf(titleTextLabel, *buttons.toTypedArray()).reversed())
            updatePositions()
        }

        val textureBundle = TexturePacker.loadFileFromResource("sprites", "sprites/spritesheet.json")

        val spriteMaterial = disposer.add(Material(Material.Config(shaderProgram, textureBundle.texture)))
        spriteMaterial.textureAutoRelease = true
        sprite1 = SpriteAtlas(spriteMaterial, textureBundle.textureAtlas)
        sprite2 = SpriteAtlas(spriteMaterial, textureBundle.textureAtlas)
        sprite1.position = centerPosition
        sprite2.position = centerPosition
        sprite1.scale = Vector3(0.5f)
        sprite2.scale = Vector3(0.5f)
        batch.add(sprite1)
        batch.add(sprite2)
    }

    override fun draw(delta: Float) {
        colorCircle += delta * 0.2f
        if (colorCircle > 1.0f) colorCircle -= 1.0f

        val color = Color.hsv2rgb(colorCircle, 1.0f, 1.0f)
        titleTextLabel.color = Vector4(color, 1.0f)

        KotchanCore.instance.graphicsApi.clearColor(Vector4(0.2f, 0.2f, 0.2f, 1.0f))
        KotchanCore.instance.graphicsApi.clearDepth(1.0f)
        KotchanCore.instance.graphicsApi.setViewport(KotchanCore.instance.viewport)

        sprite1.position = centerPosition + Vector3(
                cos(colorCircle * PI * 2.0f).toFloat() * 50.0f,
                sin(colorCircle * PI * 2.0f).toFloat() * 30.0f,
                -sin(colorCircle * PI * 2.0f).toFloat())

        batch.draw(delta, camera)
        uiBatch.draw(delta, uiCamera)
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}
}
