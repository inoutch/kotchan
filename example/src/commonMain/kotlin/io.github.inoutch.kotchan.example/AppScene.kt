package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.io.file.readBytesFromResourceWithErrorAsync
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.compatible.loadPNGByteArrayAsync
import io.github.inoutch.kotchan.core.graphic.compatible.shader.StandardShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.StandardMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

@ExperimentalStdlibApi
class AppScene(context: SceneContext) : Scene(context) {
    private val shaderProgram = StandardShaderProgram.create()

    private val camera = Camera2D.create()

    private val material = StandardMaterial.create(shaderProgram, camera, Texture.empty())

    private val batch = Batch(material)

    private var texture: Texture? = null

    private val mesh = Mesh(
            listOf(Vector3F(0.0f, 0.0f, 0.0f), Vector3F(1.0f, 0.0f, 0.0f), Vector3F(0.5f, 0.5f, 0.0f)),
            listOf(Vector2F.Zero, Vector2F.Zero, Vector2F.Zero),
            listOf(Vector4F(1.0f, 0.0f, 0.0f, 1.0f), Vector4F(0.0f, 1.0f, 0.0f, 1.0f), Vector4F(0.0f, 0.0f, 1.0f, 1.0f))
    )

    init {
        batch.add(Polygon(mesh))
    }

    override suspend fun init() {
        val pngByteArray = file.readBytesFromResourceWithErrorAsync("sprites/spritesheet.png").await()
        val image = loadPNGByteArrayAsync(pngByteArray).await()
        texture = disposer.add(Texture.loadFromImage(image))
    }

    override suspend fun update(delta: Float) {
    }

    override suspend fun render(delta: Float) {
        graphic.setViewport(config.viewport)
        graphic.clearColor(Vector4F(.3f, .3f, .3f, 1.0f))
        graphic.clearDepth(0.0f)
        batch.render()
    }
}
