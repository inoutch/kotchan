package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.error.NoSuchFileError
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.polygon.AnimatedSpriteAtlas
import io.github.inoutch.kotchan.core.graphic.polygon.Sprite
import io.github.inoutch.kotchan.core.graphic.polygon.SpriteAtlas
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.core.tool.TexturePacker
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

class AppScene : Scene() {

    private val material: Material

    private val shaderProgram: SimpleShaderProgram = SimpleShaderProgram()

    private val camera = instance.createCamera2D()

    private val sprite: AnimatedSpriteAtlas

    private val batch = Batch()

    init {
        val graphicsApi = instance.graphicsApi
        val pipeline = graphicsApi.createGraphicsPipeline(GraphicsPipeline.CreateInfo(shaderProgram))

        val texturePackerBundle = TexturePacker.loadFileFromResource("sprites", "sprites/spritesheet.json")
        material = Material(pipeline, texturePackerBundle.texture)

        val s1 = Sprite(texturePackerBundle.texture.size.toVector2())
        s1.position = Vector3(0.0f, 0.0f, -0.5f)
        val walkAnimationSet = AnimatedSpriteAtlas.AnimationSet(List(7) { it }, 0.1f)
        sprite = AnimatedSpriteAtlas(texturePackerBundle.textureAtlas,
                AnimatedSpriteAtlas.Config(listOf(walkAnimationSet)))
        sprite.anchorPoint = Vector2.Zero
        sprite.position = Vector3(0.0f, 50.0f, 0.5f)
        batch.add(s1, material)
        batch.add(sprite, material)
    }

    override fun draw(delta: Float) {
        camera.position -= Vector3(delta, 0.0f, 0.0f)
        camera.update()

        sprite.update(delta)

        instance.graphicsApi.setViewport(instance.viewport)
        batch.draw(delta, camera)
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun destroyed() {}
}
