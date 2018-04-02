package interop.graphic

import kotlinx.cinterop.*
import platform.glescommon.*
import platform.gles3.*
import platform.GLKit.*

import kotchan.view.Texture

actual class GL {
    actual fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    actual fun viewPort(x: Int, y: Int, width: Int, height: Int) {
        glViewport(x, y, width, height)
    }

    actual fun drawTriangleArrays(first: Int, count: Int) {
        glDrawArrays(GL_TRIANGLES, first, count)
    }

    actual fun createVBO(size: Int): GLVBO {
        val data = List(size, { 0.0f })
        return createVBO(data)
    }

    actual fun createVBO(data: List<Float>): GLVBO {
        memScoped{
            val buffer = alloc<GLuintVar>()
            val memData = allocArrayOf(data.map { it.toByte() }.toByteArray())

            glGenBuffers(1, buffer.ptr)
            glBindBuffer(GL_ARRAY_BUFFER, buffer.value)
            glBufferData(GL_ARRAY_BUFFER, data.size.toLong() * 4, memData.getPointer(memScope), GL_DYNAMIC_DRAW)
            return GLVBO(buffer.value)
        }
    }

    actual fun updateVBO(vbo: GLVBO, offset: Int, data: List<Float>) {}

    actual fun vertexPointer(location: GLAttribLocation, interval: Int, vbo: GLVBO) {}

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): GLShaderProgram {
        return GLShaderProgram(0)
    }

    actual fun deleteShaderProgram(shaderProgram: GLShaderProgram) {}

    actual fun useProgram(shaderProgram: GLShaderProgram) {}

    actual fun useTexture(texture: Texture?, index: Int) {}

    actual fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String) {}

    actual fun getUniform(shaderProgram: GLShaderProgram, name: String): Int {
        return 0
    }

    actual fun getAttribLocation(shaderProgram: GLShaderProgram, name: String): Int {
        return 0
    }

    actual fun debug() {}
}
