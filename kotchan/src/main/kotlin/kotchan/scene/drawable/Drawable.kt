package kotchan.scene.drawable

import interop.graphic.GLTexture
import utility.type.*

abstract class Drawable(protected val mesh: Mesh, var texture: GLTexture = GLTexture.empty) {
    var isPositionsDirty = false
        protected set
    var isColorsDirty = false
        protected set
    var isTexcoordsDirty = false
        protected set

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

    private var positionsBuffer = mesh.pos().flatten()
    private var texcoordsBuffer = mesh.tex().flatten()
    private var colorsBuffer = mesh.col().flatten()

    open val positions = {
        if (isPositionsDirty) {
            val modelMatrix = Matrix4.createTranslation(position)
            positionsBuffer = mesh.pos()
                    .map { modelMatrix * Vector4(it, 1.0f) }
                    .map { Vector3(it) }
                    .flatten()
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
}