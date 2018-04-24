package kotchan.scene.drawable

import interop.graphic.GLAttribLocation
import interop.graphic.GLCamera
import interop.graphic.GLTexture
import interop.graphic.GLVBO
import kotchan.Engine
import kotchan.scene.shader.NoColorsShaderProgram
import utility.type.*

abstract class Drawable(protected val mesh: Mesh, var texture: GLTexture = GLTexture.empty) {
    var size: Vector2 = Vector2()
        protected set
    var isPositionsDirty = false
        protected set
    var isColorsDirty = false
        protected set
    var isTexcoordsDirty = false
        protected set

    var visible = true
        set(value) {
            isPositionsDirty = (field != value)
            field = value
        }

    var position: Vector3 = Vector3()
        set(value) {
            isPositionsDirty = (field != value)
            field = value
        }

    var color: Vector4 = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        set(value) {
            isColorsDirty = (field != value)
            field = value
        }

    var anchorPoint = Vector2(0.5f, 0.5f)
        set(value) {
            isPositionsDirty = (field != value)
            field = value
        }

    private val gl = Engine.getInstance().gl
    private var positionsBuffer = mesh.pos().flatten()
    private var texcoordsBuffer = mesh.tex().flatten()
    private var colorsBuffer = mesh.col().flatten()
    private var vbo: GLVBO? = null

    open val positions = {
        if (isPositionsDirty) {
            val modelMatrix = Matrix4.createTranslation(translate())
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

    private fun translate(position: Vector3 = this.position): Vector3 {
        return Vector3(
                position.x - size.x * anchorPoint.x,
                position.y - size.y * anchorPoint.y,
                position.z
        )
    }

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

    open fun draw(delta: Float, shaderProgram: NoColorsShaderProgram, camera: GLCamera) {
        if (!visible) {
            return
        }

        val vbo = vbo ?: return
        shaderProgram.use()
        shaderProgram.modelMatrix4 = Matrix4.createTranslation(translate())
        shaderProgram.prepare(delta, camera)
        texture.use()

        gl.bindVBO(vbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 5, 0)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 5, 3)

        gl.drawTriangleArrays(0, mesh.size)
    }

    fun destroy() {
        vbo?.destroy()
        texture.destroy()
    }
}