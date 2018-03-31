package kotchan.view.drawable

import interop.graphic.SimpleFragmentShaderSource
import interop.graphic.SimpleVertexShaderSource
import kotchan.Engine
import utility.type.Mesh
import utility.type.Size

class Square(size: Size) :
        Drawable(Engine.getInstance().shaderManager.getShaderProgram(SimpleVertexShaderSource, SimpleFragmentShaderSource),
                createSquareMesh(size)) {
    companion object {
        fun createSquareMesh(size: Size) = Mesh(
                mutableListOf(
                        0.0f, 0.0f, 0.0f,
                        0.0f, size.height, 0.0f,
                        size.width, size.height, 0.0f,
                        0.0f, 0.0f, 0.0f,
                        size.width, size.height, 0.0f,
                        size.width, 0.0f, 0.0f),
                mutableListOf(
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 1.0f),
                mutableListOf(
                        1.0f, 0.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f)
        )
    }
}