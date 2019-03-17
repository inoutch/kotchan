package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.controller.touch.listener.ScrollTouchListener
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.polygon.AnimatedSpriteAtlas
import io.github.inoutch.kotchan.core.graphic.polygon.Sprite
import io.github.inoutch.kotchan.core.graphic.shader.AlphaTestSimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.graphic.template.Template
import io.github.inoutch.kotchan.core.graphic.template.TemplateAppendType
import io.github.inoutch.kotchan.core.graphic.template.TemplateType
import io.github.inoutch.kotchan.core.tool.TexturePacker
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class AppScene : Scene() {

    private val material: Material

    private val shaderProgram = AlphaTestSimpleShaderProgram()

    private val camera = instance.createCamera2D()

    private val sprite1: Sprite
    private val sprite2: Sprite
    private val sprite3: Sprite

    private val batch = Batch()

    init {
        val bmFont = BMFont.loadFromResource("font/sample.fnt")

        val graphicsApi = instance.graphicsApi
        val pipeline = graphicsApi.createGraphicsPipeline(GraphicsPipeline.CreateInfo(shaderProgram))

        material = Material(pipeline, graphicsApi.loadTextureFromResource("tiles/sample.png"))

        sprite1 = Sprite(Vector2(45, 32))
        sprite2 = Sprite(Vector2(32, 45))
        sprite3 = Sprite(Vector2(53, 101))

        batch.add(material, sprite1, sprite2, sprite3)

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
