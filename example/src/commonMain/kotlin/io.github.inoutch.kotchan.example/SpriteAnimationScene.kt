package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.compatible.shader.StandardShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.StandardMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.AnimatedSpriteAtlas
import io.github.inoutch.kotchan.core.tool.TexturePacker
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

@ExperimentalStdlibApi
class SpriteAnimationScene(context: SceneContext) : Scene(context) {
    private val camera = Camera2D.create()

    private val shaderProgram = StandardShaderProgram.create()

    private var sprite: AnimatedSpriteAtlas? = null

    private var batch: Batch? = null

    override suspend fun init() {
        val bundle = TexturePacker.loadFromResourceWithError("sprites", "sprites/spritesheet.json")
        disposer.add(bundle.texture)

        val animations = listOf(AnimatedSpriteAtlas.AnimationSet(List(8) { "go_${it + 1}.png" }, 0.1f))
        val material = StandardMaterial.create(shaderProgram, camera, bundle.texture)
        val sprite = AnimatedSpriteAtlas(bundle.textureAtlas, AnimatedSpriteAtlas.Config(animations))
        sprite.position = Vector3F(config.screenSize / 2.0f, 0.0f)

        val batch = Batch(material)
        disposer.add(batch)
        batch.add(sprite)

        this.batch = batch
        this.sprite = sprite
    }

    override suspend fun update(delta: Float) {
    }

    override suspend fun render(delta: Float) {
        graphic.setScissor(RectI(Vector2I.Zero, config.viewportSize))
        graphic.setViewport(config.viewport)
        graphic.clearColor(Vector4F.Zero)
        graphic.clearDepth(1.0f)

        sprite?.update(delta)
        batch?.render()
    }
}
