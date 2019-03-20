package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.controller.touch.listener.ScrollTouchListener
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.polygon.Sprite
import io.github.inoutch.kotchan.core.graphic.polygon.TextLabel
import io.github.inoutch.kotchan.core.graphic.shader.AlphaTestSimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.template.Template
import io.github.inoutch.kotchan.core.graphic.template.TemplateAppendType
import io.github.inoutch.kotchan.core.graphic.template.TemplateType
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

class AppScene : Scene() {

    private val material: Material

    private val shaderProgram = AlphaTestSimpleShaderProgram()

    private val camera = instance.createCamera2D()

    private val sprite1: Sprite
    private val sprite2: Sprite
    private val sprite3: Sprite

//    private val label: TextLabel

    private val batch = Batch()

    init {
//        val textLabelMaterialConfig = Material.Config(shaderProgram)
//        label = TextLabel.loadFromResource(
//                "font/sample.fnt", "font", textLabelMaterialConfig, "Kotchan Examples")
//        label.position = Vector3(100.0f, 100.0f, 0.0f)
//        batch.add(label)

        val spriteMaterialConfig = Material.Config(shaderProgram, Texture.loadFromResource("tiles/sample.png"))
        material = Material(spriteMaterialConfig)

        sprite1 = Sprite(material, Vector2(45, 32))
        sprite2 = Sprite(material, Vector2(32, 45))
        sprite3 = Sprite(material, Vector2(53, 101))

        batch.add(sprite1, sprite2)

        val template = Template()
        template.add(TemplateType.BottomCenter,
                TemplateAppendType.Row,
                8.0f, 0.0f,
                listOf(sprite1, sprite2, sprite3))
        template.updatePositions()

        ScrollTouchListener(camera) {
            camera.position -= Vector3(it.x, -it.y, 0.0f)
            camera.update()
        }.also {
            it.accelerationEnable = true
            touchController.add(it)
        }
    }

    override fun draw(delta: Float) {
        instance.graphicsApi.setViewport(instance.viewport)
        batch.draw(delta, camera)
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun dispose() {}
}
