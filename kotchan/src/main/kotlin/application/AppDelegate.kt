package application

import interop.graphic.GLAttribLocation
import interop.graphic.SimpleFragmentShaderSource
import interop.graphic.SimpleVertexShaderSource
import kotchan.Engine
import kotchan.view.batch.Batch
import kotchan.view.View
import kotchan.view.drawable.Square
import utility.type.Size

class AppDelegate : View() {
    private val square: Square = Square(Size(32.0f, 32.0f))
    private val spriteBatch = Batch().apply {
        add(square)
    }

    private val gl = Engine.getInstance().gl
    private val mesh = Square.createSquareMesh(Size(10.0f, 10.0f))
    private val p = gl.createVBO(mesh.vertices)
    private val t = gl.createVBO(mesh.texcoords)
    private val c = gl.createVBO(mesh.colors)
    private val shader = gl.compileShaderProgram(SimpleVertexShaderSource, SimpleFragmentShaderSource)

    override fun render(delta: Float) {
        gl.debug()
        gl.clearColor(0.0f, 1.0f, 1.0f, 1.0f)
        shader.use()
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, p)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_COLOR, 4, c)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, t)
        gl.drawTriangleArrays(0, 6)

        //spriteBatch.draw(delta)
    }

    override fun pause() {}

    override fun resume() {}
}