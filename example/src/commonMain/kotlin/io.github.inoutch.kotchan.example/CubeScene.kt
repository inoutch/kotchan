package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.core.graphic.batch.Batch3D
import io.github.inoutch.kotchan.core.graphic.camera.Camera3D
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Standard3DShaderProgram
import io.github.inoutch.kotchan.core.graphic.material.Standard3DMaterial
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

class CubeScene(context: SceneContext) : Scene(context) {
    private val mesh = Mesh(
            listOf(
                    Vector3F(-1.0f, -1.0f, -1.0f),//
                    Vector3F(-1.0f, -1.0f, 1.0f),
                    Vector3F(-1.0f, 1.0f, 1.0f),
                    Vector3F(-1.0f, -1.0f, -1.0f),//
                    Vector3F(-1.0f, 1.0f, 1.0f),
                    Vector3F(-1.0f, 1.0f, -1.0f),

                    Vector3F(1.0f, 1.0f, -1.0f),//
                    Vector3F(-1.0f, -1.0f, -1.0f),
                    Vector3F(-1.0f, 1.0f, -1.0f),
                    Vector3F(1.0f, 1.0f, -1.0f),//
                    Vector3F(1.0f, -1.0f, -1.0f),
                    Vector3F(-1.0f, -1.0f, -1.0f),

                    Vector3F(1.0f, -1.0f, 1.0f),//
                    Vector3F(-1.0f, -1.0f, -1.0f),
                    Vector3F(1.0f, -1.0f, -1.0f),
                    Vector3F(1.0f, -1.0f, 1.0f),//
                    Vector3F(-1.0f, -1.0f, 1.0f),
                    Vector3F(-1.0f, -1.0f, -1.0f),

                    Vector3F(1.0f, 1.0f, 1.0f),//
                    Vector3F(1.0f, -1.0f, -1.0f),
                    Vector3F(1.0f, 1.0f, -1.0f),
                    Vector3F(1.0f, -1.0f, -1.0f),//
                    Vector3F(1.0f, 1.0f, 1.0f),
                    Vector3F(1.0f, -1.0f, 1.0f),

                    Vector3F(1.0f, 1.0f, 1.0f),//
                    Vector3F(1.0f, 1.0f, -1.0f),
                    Vector3F(-1.0f, 1.0f, -1.0f),
                    Vector3F(1.0f, 1.0f, 1.0f),//
                    Vector3F(-1.0f, 1.0f, -1.0f),
                    Vector3F(-1.0f, 1.0f, 1.0f),

                    Vector3F(-1.0f, 1.0f, 1.0f),//
                    Vector3F(-1.0f, -1.0f, 1.0f),
                    Vector3F(1.0f, -1.0f, 1.0f),
                    Vector3F(1.0f, 1.0f, 1.0f),//
                    Vector3F(-1.0f, 1.0f, 1.0f),
                    Vector3F(1.0f, -1.0f, 1.0f)
            ),
            listOf(
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f),
                    Vector2F(0.0f, 0.0f)
            ),
            listOf(
                    Vector4F(1.0f, 0.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 0.0f, 1.0f),

                    Vector4F(0.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 0.0f, 1.0f),

                    Vector4F(0.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 0.0f, 1.0f, 1.0f),

                    Vector4F(1.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 1.0f, 0.0f, 1.0f),
                    Vector4F(1.0f, 1.0f, 0.0f, 1.0f),

                    Vector4F(1.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 1.0f, 1.0f),
                    Vector4F(1.0f, 0.0f, 1.0f, 1.0f),

                    Vector4F(0.0f, 1.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 1.0f, 1.0f),
                    Vector4F(0.0f, 1.0f, 1.0f, 1.0f)
            ),
            listOf(
                    Vector3F(-1.0f, +0.0f, +0.0f),
                    Vector3F(-1.0f, +0.0f, +0.0f),
                    Vector3F(-1.0f, +0.0f, +0.0f),
                    Vector3F(-1.0f, +0.0f, +0.0f),
                    Vector3F(-1.0f, +0.0f, +0.0f),
                    Vector3F(-1.0f, +0.0f, +0.0f),

                    Vector3F(+0.0f, +0.0f, -1.0f),
                    Vector3F(+0.0f, +0.0f, -1.0f),
                    Vector3F(+0.0f, +0.0f, -1.0f),
                    Vector3F(+0.0f, +0.0f, -1.0f),
                    Vector3F(+0.0f, +0.0f, -1.0f),
                    Vector3F(+0.0f, +0.0f, -1.0f),

                    Vector3F(+0.0f, -1.0f, +0.0f),
                    Vector3F(+0.0f, -1.0f, +0.0f),
                    Vector3F(+0.0f, -1.0f, +0.0f),
                    Vector3F(+0.0f, -1.0f, +0.0f),
                    Vector3F(+0.0f, -1.0f, +0.0f),
                    Vector3F(+0.0f, -1.0f, +0.0f),

                    Vector3F(+1.0f, +0.0f, +0.0f),
                    Vector3F(+1.0f, +0.0f, +0.0f),
                    Vector3F(+1.0f, +0.0f, +0.0f),
                    Vector3F(+1.0f, +0.0f, +0.0f),
                    Vector3F(+1.0f, +0.0f, +0.0f),
                    Vector3F(+1.0f, +0.0f, +0.0f),

                    Vector3F(+0.0f, +1.0f, +0.0f),
                    Vector3F(+0.0f, +1.0f, +0.0f),
                    Vector3F(+0.0f, +1.0f, +0.0f),
                    Vector3F(+0.0f, +1.0f, +0.0f),
                    Vector3F(+0.0f, +1.0f, +0.0f),
                    Vector3F(+0.0f, +1.0f, +0.0f),

                    Vector3F(+0.0f, +0.0f, +1.0f),
                    Vector3F(+0.0f, +0.0f, +1.0f),
                    Vector3F(+0.0f, +0.0f, +1.0f),
                    Vector3F(+0.0f, +0.0f, +1.0f),
                    Vector3F(+0.0f, +0.0f, +1.0f),
                    Vector3F(+0.0f, +0.0f, +1.0f)
            )
    )

    private val polygon = Polygon(mesh)

    private val camera = Camera3D.create()

    private val batch: Batch3D

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
        batch.add(polygon)

        camera.position = Vector3F(0.0f, 2.0f, -4.0f)
        camera.targetPosition = Vector3F.Zero
        camera.up = Vector3F(0.0f, 1.0f, 0.0f)
        camera.update()

        polygon.rotationAxis = Vector3F(-1.0f, 1.0f, -1.0f)
    }

    override suspend fun init() {
    }

    override suspend fun update(delta: Float) {
        polygon.rotationRadian += delta
    }

    override suspend fun render(delta: Float) {
        graphic.setScissor(RectI(Vector2I.Zero, KotchanGlobalContext.config.viewportSize))
        graphic.setViewport(KotchanGlobalContext.config.viewport)
        graphic.clearColor(Vector4F(.3f, .3f, .3f, 1.0f))
        graphic.clearDepth(1.0f)

        batch.render()
    }
}
