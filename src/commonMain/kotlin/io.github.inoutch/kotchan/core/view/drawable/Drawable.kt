package io.github.inoutch.kotchan.core.view.drawable

import io.github.inoutch.kotchan.utility.graphic.GLAttribLocation
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.utility.graphic.GLTexture
import io.github.inoutch.kotchan.utility.graphic.GLVBO
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction
import io.github.inoutch.kotchan.core.model.Model
import io.github.inoutch.kotchan.core.view.shader.NoColorsShaderProgram
import io.github.inoutch.kotchan.utility.type.*

open class Drawable(initMesh: Mesh, var texture: GLTexture = GLTexture.empty) : StrictDestruction(), DrawableBase {
    var mesh = initMesh
        protected set

    override var size = Vector2()
        protected set

    var isPositionsDirty = true
        protected set

    var isColorsDirty = true
        protected set

    var isTexcoordsDirty = true
        protected set

    override var visible = true
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    override var position = Vector3()
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    var color = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value)
                isColorsDirty = true
            field = value
        }

    override var anchorPoint = Vector2(0.5f, 0.5f)
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    var scale = Vector3(1.0f, 1.0f, 1.0f)
        set(value) {
            if (field != value) {
                isPositionsDirty = true
            }
            field = value
        }

    private val gl = KotchanCore.instance.gl
    private var positionsBuffer = mesh.pos().flatten()
    private var texcoordsBuffer = mesh.tex().flatten()
    private var colorsBuffer = mesh.col().flatten()
    private var vbo: GLVBO? = null

    open val positions = {
        if (isPositionsDirty) {
            val modelMatrix = transform()
            val pos = mesh.pos()
            positionsBuffer = if (visible) {
                pos.map { modelMatrix * Vector4(it, 1.0f) }.map { Vector3(it) }
            } else {
                pos.map { Vector3() }

            }.flatten()
            isPositionsDirty = false
        }
        positionsBuffer
    }

    open val colors = {
        if (isColorsDirty) {
            colorsBuffer = mesh.col()
                    .map { color }
                    .flatten()
            isColorsDirty = false
        }
        colorsBuffer
    }

    open val texcoords = {
        if (isTexcoordsDirty) {
            texcoordsBuffer = mesh.tex().flatten()
            isTexcoordsDirty = false
        }
        texcoordsBuffer
    }

    private fun transform() = Matrix4.createTranslation(position) *
            Matrix4.createScale(scale) * Matrix4.createTranslation(Vector3(size * anchorPoint * -1.0f, 0.0f))

    private fun vertices(): FloatArray {
        val vertices: MutableList<Float> = mutableListOf()
        for (i in 0 until mesh.size) {
            mesh.getVertex(i)?.let {
                vertices.add(it.position.x)
                vertices.add(it.position.y)
                vertices.add(it.position.z)

                vertices.add(it.texcoord.x)
                vertices.add(it.texcoord.y)
            }
        }
        return vertices.toFloatArray()
    }

    fun bind() {
        vbo = gl.createVBO(vertices())
    }

    open fun draw(delta: Float, shaderProgram: NoColorsShaderProgram, camera: Camera) {
        if (!visible) {
            return
        }

        val vbo = vbo ?: return
        shaderProgram.use()
        shaderProgram.modelMatrix4 = transform()
        shaderProgram.prepare(delta, camera.combine)
        texture.use()

        gl.bindVBO(vbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 5, 0)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 5, 3)

        gl.drawTriangleArrays(0, mesh.size)
    }

    override fun update(delta: Float) {}

    override fun destroy() {
        super.destroy()

        vbo?.destroy()
        texture.destroy()
    }

    fun replaceMesh(mesh: Mesh) {
        this.mesh = mesh
        isPositionsDirty = true
        isTexcoordsDirty = true
        isColorsDirty = true
    }
}