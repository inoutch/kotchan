package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.graphic.batch.Batch
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.io.file.readBytesFromResourceWithErrorAsync
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext

@ExperimentalStdlibApi
class AppScene(context: SceneContext) : Scene(context) {
    private val batch = disposer.add(Batch())

    private var texture: Texture? = null

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
