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
import io.github.inoutch.kotchan.core.tool.TexturePacker
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class AppScene : Scene() {

    private val material: Material

    private val shaderProgram = AlphaTestSimpleShaderProgram()

    private val camera = instance.createCamera2D()

    private val sprite1: AnimatedSpriteAtlas
    private val sprite2: Sprite

    private val batch = Batch()

    init {
        val graphicsApi = instance.graphicsApi
        val pipeline = graphicsApi.createGraphicsPipeline(GraphicsPipeline.CreateInfo(shaderProgram))

        val texturePackerBundle = TexturePacker.loadFileFromResource("sprites", "sprites/spritesheet.json")
        material = Material(pipeline, texturePackerBundle.texture)

        sprite2 = Sprite(texturePackerBundle.texture.size.toVector2())
        sprite2.position = Vector3(0.0f, 0.0f, -0.5f)
        val walkAnimationSet = AnimatedSpriteAtlas.AnimationSet(List(7) { it }, 0.1f)
        sprite1 = AnimatedSpriteAtlas(texturePackerBundle.textureAtlas,
                AnimatedSpriteAtlas.Config(listOf(walkAnimationSet)))

        sprite1.anchorPoint = Vector2.Zero
        sprite1.position = Vector3(0.0f, 50.0f, 0.5f)
        batch.add(sprite1, material)
        batch.add(sprite2, material)

        ScrollTouchListener(camera) {
            camera.position -= Vector3(it.x, -it.y, 0.0f)
            camera.update()
        }.also {
            it.accelerationEnable = true
            touchController.add(it)
        }
    }

    override fun draw(delta: Float) {

        sprite1.update(delta)

        instance.graphicsApi.setViewport(instance.viewport)
        batch.draw(delta, camera)
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun destroyed() {}
}
