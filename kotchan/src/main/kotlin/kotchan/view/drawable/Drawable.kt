package kotchan.view.drawable

import interop.graphic.GLTexture
import utility.type.*

abstract class Drawable(private val mesh: Mesh, var texture: GLTexture = GLTexture.empty) {
    var isPositionsDirty = false
        private set
    var isColorsDirty = false
        private set
    var isTexcoordsDirty = false
        private set

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

    private var positionsBuffer = mesh.vertices.flatten()
    private var texcoordsBuffer = mesh.texcoords.flatten()
    private var colorsBuffer = mesh.colors.flatten()

    open val positions = {
        if (isPositionsDirty) {
            val modelMatrix = Matrix4.createTranslation(position)
            positionsBuffer = mesh.vertices
                    .map { modelMatrix * Vector4(it, 1.0f) }
                    .map { Vector3(it) }
                    .flatten()
            isPositionsDirty = false
        }
        positionsBuffer
    }

    open val colors = {
        if (isColorsDirty) {
            colorsBuffer = mesh.colors
                    .map { color }
                    .flatten()
        }
        colorsBuffer
    }

    open val texcoords = {
        texcoordsBuffer
    }
}