package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Standard2DShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.Standard2DMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.SpriteAtlas
import io.github.inoutch.kotchan.core.tool.TexturePacker
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

class SpriteAtlasScene(context: SceneContext) : Scene(context) {
    private val shaderProgram = Standard2DShaderProgram.create()

    private val camera = Camera2D.create()

    private var batch: Batch? = null

    @ExperimentalStdlibApi
    override suspend fun init() {
        val bundle = TexturePacker.loadFromResourceWithError("sprites", "sprites/counter.json")
        val material = Standard2DMaterial.create(shaderProgram, camera, bundle.texture)
        val batch = disposer.add(Batch(material))
        val polygon = SpriteAtlas(bundle.textureAtlas)

        polygon.position = (config.screenSize / 2.0f).toVector3F()
        polygon.setAtlas(0)
        polygon.scale = Vector3F(10.0f)

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
