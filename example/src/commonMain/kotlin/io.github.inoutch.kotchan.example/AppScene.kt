package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.io.file.readBytesFromResourceWithErrorAsync
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.core.graphic.compatible.Material
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotchan.math.flatten

@ExperimentalStdlibApi
class AppScene(context: SceneContext) : Scene(context) {
    private val batch = disposer.add(Batch(Material()))

    private var texture: Texture? = null

    private val mesh = Mesh(
            listOf(Vector3F(0.5f, 1.0f, .0f), Vector3F(1.0f, 0.0f, .0f), Vector3F(0.0f, 0.0f, .0f)),
            listOf(Vector2F.Zero, Vector2F.Zero, Vector2F.Zero),
            listOf(Vector4F(1.0f, 0.0f, 0.0f, 1.0f), Vector4F(0.0f, 1.0f, 0.0f, 1.0f), Vector4F(0.0f, 0.0f, 1.0f, 1.0f))
    )

    private val vertexBuffer = VertexBuffer.create(mesh.pos().flatten(), BufferStorageMode.Dynamic)

    override suspend fun init() {
        val pngByteArray = file.readBytesFromResourceWithErrorAsync("sprites/spritesheet.png").await()
        val image = Image.loadPNGByteArrayAsync(pngByteArray).await()
        texture = disposer.add(Texture.loadFromImage(image))
    }

    override suspend fun update(delta: Float) {
    }

    override suspend fun render(delta: Float) {
    }
}
