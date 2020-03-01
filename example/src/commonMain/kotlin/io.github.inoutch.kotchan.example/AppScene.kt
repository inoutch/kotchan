package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.core.graphic.batch.LabelBatch
import io.github.inoutch.kotchan.core.graphic.camera.Camera2D
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.StandardLabelShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.StandardLabelMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.LabelSprite
import io.github.inoutch.kotchan.core.tool.BMFontLoader
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotchan.utility.Path

@ExperimentalStdlibApi
class AppScene(context: SceneContext) : Scene(context) {
    private val shaderProgram = StandardLabelShaderProgram()

    private val camera = Camera2D.create()

    private var batch: LabelBatch? = null

    override suspend fun init() {
        val bmFont = BMFontLoader.loadFromResourceWithError("font/sample.fnt")
        val label = LabelSprite.create("Hello World!", bmFont, 60.0f)
        //val textures = bmFont.pages.map { Texture.loadFromResourceWithError(Path.resolve("font", it.file)) }
        val batch = disposer.add(LabelBatch(StandardLabelMaterial.create(shaderProgram, camera, emptyList())))
        batch.add(label, 0)

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
