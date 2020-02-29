package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.batch.Batch3D
import io.github.inoutch.kotchan.core.graphic.camera.Camera3D
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Standard3DShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.Standard3DMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.core.io.file.readTextFromResourceWithErrorAsync
import io.github.inoutch.kotchan.core.tool.OBJLoader
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

@ExperimentalStdlibApi
class OBJScene(context: SceneContext) : Scene(context) {
    private val camera = Camera3D.create()

    private val batch: Batch3D

    private var polygon: Polygon? = null

    init {
        val shaderProgram = disposer.add(Standard3DShaderProgram.create())
        val material = Standard3DMaterial.create(
                shaderProgram,
                camera,
                Texture.empty(),
                Vector3F(0.0f, 0.0f, -1.0f),
                Vector3F(1.0f, 1.0f, 1.0f)
        )
        batch = disposer.add(Batch3D(material))

        camera.position = Vector3F(0.0f, 1.0f, -4.0f)
        camera.targetPosition = Vector3F.Zero
        camera.up = Vector3F(0.0f, 1.0f, 0.0f)
        camera.update()
    }

    override suspend fun init() {
        val objSource = file.readTextFromResourceWithErrorAsync("obj/monkey.obj").await()
        val bundle = OBJLoader.loadObj(objSource)
        val polygon = Polygon(bundle.objects.first().toMesh())
        polygon.rotationAxis = Vector3F(0.0f, 1.0f, 0.0f)

        batch.add(polygon)

        this.polygon = polygon
    }

    override suspend fun update(delta: Float) {
        polygon?.let { it.rotationRadian += delta }
    }

    override suspend fun render(delta: Float) {
        graphic.setScissor(RectI(Vector2I.Zero, KotchanGlobalContext.config.viewportSize))
        graphic.setViewport(KotchanGlobalContext.config.viewport)
        graphic.clearColor(Vector4F(.3f, .3f, .3f, 1.0f))
        graphic.clearDepth(1.0f)

        batch.render()
    }
}
