package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.TextureFilter
import io.github.inoutch.kotchan.core.graphic.compatible.shader.StandardShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.StandardMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

@ExperimentalStdlibApi
class TriangleScene(context: SceneContext) : Scene(context) {
    private val shaderProgram = StandardShaderProgram.create()

    private val camera = Camera2D.create()

    private var batch: Batch? = null

    private val mesh = Mesh(
            listOf(Vector3F(0.0f, 0.0f, 0.0f), Vector3F(500.0f, 0.0f, 0.0f), Vector3F(250.0f, 500.0f, 0.0f)),
            listOf(Vector2F(0.0f, 1.0f), Vector2F(1.0f, 1.0f), Vector2F(0.5f, 0.0f)),
            listOf(Vector4F(1.0f, 1.0f, 1.0f, 1.0f), Vector4F(1.0f, 1.0f, 1.0f, 1.0f), Vector4F(1.0f, 1.0f, 1.0f, 1.0f))
    )

    override suspend fun init() {
        val pixels = byteArrayOf(
                -1, -1, -1, -1, 127, 127, 127, -1,
                127, 127, 127, -1, -1, -1, -1, -1
        )
        val image = Image(pixels, Vector2I(2, 2))
        val textureConfig = Texture.Config(
                magFilter = TextureFilter.NEAREST,
                minFilter = TextureFilter.NEAREST
        )
        val texture = disposer.add(Texture.load(image, textureConfig))
        val material = StandardMaterial.create(shaderProgram, camera, texture)
        val batch = disposer.add(Batch(material))

        val polygon1 = Polygon(mesh) // Red
        val polygon2 = Polygon(mesh) // Blue

        // Use depth test
        polygon1.position = Vector3F(50.0f, 0.0f, 0.0f)
        polygon1.color = Vector4F(1.0f, 0.0f, 0.0f, 1.0f)
        polygon2.position = Vector3F(0.0f, 0.0f, 10.0f)
        polygon2.color = Vector4F(0.0f, 0.0f, 1.0f, 1.0f)

        batch.add(polygon1, 1)
        batch.add(polygon2, 0)
        batch.sortByRenderOrder()

        this.batch = batch
    }

    override suspend fun update(delta: Float) {
    }

    override suspend fun render(delta: Float) {
        graphic.setScissor(RectI(Vector2I.Zero, config.viewportSize))
        graphic.setViewport(config.viewport)
        graphic.clearColor(Vector4F(.3f, .3f, .3f, 1.0f))
        graphic.clearDepth(1.0f)
        batch?.render()
    }
}
