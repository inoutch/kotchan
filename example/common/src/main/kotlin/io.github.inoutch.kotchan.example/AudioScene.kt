package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.polygon.TextLabel
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.template.Template
import io.github.inoutch.kotchan.core.graphic.template.TemplateAppendType
import io.github.inoutch.kotchan.core.graphic.template.TemplateType
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.core.graphic.ui.button.ColorButton
import io.github.inoutch.kotchan.utility.audio.BGM
import io.github.inoutch.kotchan.utility.audio.loadFromResource
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.type.Color
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class AudioScene : Scene() {

    private val shaderProgram = SimpleShaderProgram()

    private val camera = KotchanCore.instance.createCamera2D()

    private val bgm = disposer.add(BGM.loadFromResource("sample.wav"))

    private val buttonMaterial: Material

    private val titleTextLabel: TextLabel

    private val batch = disposer.add(Batch())

    private var colorCircle = 0.0f

    init {

        val bmFont = disposer.add(BMFont.loadFromResource(
                "font/sample.fnt", "font", Material.Config(shaderProgram)))
        titleTextLabel = TextLabel(bmFont, "Audio Examples")

        buttonMaterial = disposer.add(Material(Material.Config(shaderProgram, Texture.emptyTexture())))

        val transitions = listOf("Play" to {
            bgm.play()
        }, "Stop" to {
            bgm.stop()
        }, "Pause" to {
            bgm.pause()
        }, "Back" to {
            instance.runScene { AppScene() }
        })
        val buttons = transitions.map {
            val button = ColorButton(buttonMaterial, camera, Vector2(250, 32), it.second)
            button.normalColor = Vector4(0.0f, 0.0f, 0.0f, 0.2f)
            button.pressedColor = Vector4(0.0f, 0.0f, 0.0f, 0.4f)
            touchController.add(button.touchListener)

            val label = TextLabel(bmFont, it.first)
            label.position = Vector3(0.0f, 0.0f, -0.1f)
            button.addChild(label)
            button
        }

        batch.add(titleTextLabel, *buttons.toTypedArray())

        Template().apply {
            add(TemplateType.MiddleCenter, TemplateAppendType.Row, 12.0f, 0.0f,
                    listOf(titleTextLabel, *buttons.toTypedArray()).reversed())
            updatePositions()
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
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}
}
