package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Standard2DShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.Standard2DMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.Sprite
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector4F

class TextureScene(context: SceneContext) : Scene(context) {
    private val shaderProgram = Standard2DShaderProgram.create()

    private val camera = Camera2D.create()

    private var batch: Batch? = null

    @ExperimentalStdlibApi
    override suspend fun init() {
        val texture = Texture.loadFromResourceWithError("sprites/counter.png")
        val material = Standard2DMaterial.create(shaderProgram, camera, texture)
        val batch = disposer.add(Batch(material))
        val polygon = Sprite(Vector2F(400.0f, 400.0f))
        polygon.position = (config.screenSize / 2.0f).toVector3F()
        batch.add(polygon)

        this.batch = batch
    }

    override suspend fun render(delta: Float) {
        graphic.setScissor(RectI(Vector2I.Zero, config.viewportSize))
        graphic.setViewport(config.viewport)
        graphic.clearColor(Vector4F(.3f, .3f, .3f, 1.0f))
        graphic.clearDepth(1.0f)

        batch?.render()
    }

    override suspend fun update(delta: Float) {
    }
}
