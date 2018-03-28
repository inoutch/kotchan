package kotchan.view.drawable

import interop.graphic.SimpleFragmentShaderSource
import interop.graphic.SimpleVertexShaderSource
import kotchan.Engine
import utility.type.Mesh
import utility.type.Size

class Square(size: Size) : Drawable(Engine.getInstance().gl.compileShaderProgram(SimpleVertexShaderSource, SimpleFragmentShaderSource)) {
    init {
        this.mesh = Mesh().apply {
            vertices.addAll(arrayOf(
                    0.0f, 0.0f, 0.0f,
                    0.0f, size.height, 0.0f,
                    size.width, size.height, 0.0f,
                    size.width, 0.0f, 0.0f))

            texcoords.addAll(arrayOf(
                    0.0f, 1.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 1.0f))

            colors.addAll(arrayOf(
                    1.0f, 0.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 1.0f))
        }
    }
}