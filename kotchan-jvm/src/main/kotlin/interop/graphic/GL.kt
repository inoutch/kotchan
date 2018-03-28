package interop.graphic

import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.GL4ES3
import com.jogamp.opengl.GLArrayData
import com.jogamp.opengl.GLES3
import com.jogamp.opengl.util.GLArrayDataClient
import kotchan.view.Texture
import java.nio.ByteBuffer
import java.nio.IntBuffer

actual class GL {
    private val gl: GL4ES3

    init {
        if (JoglProvider.gl == null) {
            throw RuntimeException("not load jogl")
        }
        gl = JoglProvider.gl as GL4ES3
    }

    actual fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        gl.glClear(GL4ES3.GL_COLOR_BUFFER_BIT)
        gl.glClearColor(red, green, blue, alpha)
    }

    actual fun viewPort(x: Int, y: Int, width: Int, height: Int) {
        gl.glViewport(x, y, width, height)
    }

    actual fun vertexPointer(location: GLAttribLocation, interval: Int, vbo: GLVBO) {
        gl.glBindBuffer(GLES3.GL_ARRAY_BUFFER, vbo.id)
        gl.glEnableVertexAttribArray(location.value)
        gl.glVertexAttribPointer(location.value, interval, GL4ES3.GL_FLOAT, false, 0, 0)
    }

    actual fun drawTriangleArrays(first: Int, count: Int) {
        gl.glDrawArrays(GLES3.GL_TRIANGLES, first, count)
    }

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): GLShaderProgram {
        return gl.run {
            val vertexShader = compileShader(GL4ES3.GL_VERTEX_SHADER, vertexShaderText)
            val fragmentShader = compileShader(GL4ES3.GL_FRAGMENT_SHADER, fragmentShaderText)

            val shaderProgram = glCreateProgram()
            glAttachShader(shaderProgram, vertexShader)
            glAttachShader(shaderProgram, fragmentShader)
            glLinkProgram(shaderProgram)
            glValidateProgram(shaderProgram)

            if (vertexShader != 0) {
                glDetachShader(shaderProgram, vertexShader)
                glDeleteShader(vertexShader)
            }
            if (fragmentShader != 0) {
                glDetachShader(shaderProgram, fragmentShader)
                glDeleteShader(fragmentShader)
            }
            return@run GLShaderProgram(shaderProgram)
        }
    }

    actual fun useProgram(shaderProgram: GLShaderProgram) {
        gl.glUseProgram(shaderProgram.id)
    }

    actual fun useTexture(texture: Texture?, index: Int) {
        gl.glBindTexture(texture?.id ?: 0, index)
    }

    actual fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String) {
        gl.glBindAttribLocation(shaderProgram.id, attributeLocation.value, name)
    }

    actual fun getUniform(shaderProgram: GLShaderProgram, name: String): Int {
        return gl.glGetUniformLocation(shaderProgram.id, name)
    }

    actual fun createVBO(data: List<Float>): GLVBO {
        val vertexBuffer = Buffers.newDirectFloatBuffer(data.toFloatArray())
        val id = arrayOf(0).toIntArray()
        gl.glGenBuffers(1, id, 0)
        gl.glBindBuffer(GL4ES3.GL_ARRAY_BUFFER, id.first())
        gl.glBufferData(GL4ES3.GL_ARRAY_BUFFER, data.size.toLong(), vertexBuffer, GL4ES3.GL_DYNAMIC_DRAW)
        return GLVBO(id.first())
    }

    actual fun createVBO(size: Int): GLVBO {
        val data = List(size, { 0.0f })
        return createVBO(data)
    }

    actual fun updateVBO(vbo: GLVBO, offset: Int, data: List<Float>) {
        val vertexBuffer = Buffers.newDirectFloatBuffer(data.toFloatArray())
        gl.glBindBuffer(GL4ES3.GL_ARRAY_BUFFER, vbo.id)
        gl.glBufferSubData(GL4ES3.GL_ARRAY_BUFFER, offset.toLong(), data.size.toLong(), vertexBuffer)
    }

    private fun compileShader(type: Int, text: String): Int = gl.run {
        val shader = glCreateShader(type)
        glShaderSource(shader, 1, arrayOf(text), null)
        glCompileShader(shader)

        val status = IntBuffer.allocate(1)
        glGetShaderiv(shader, GL4ES3.GL_COMPILE_STATUS, status)

        if (status.get(0) == 0) {
            val logLength = IntBuffer.allocate(1)
            glGetShaderiv(shader, GL4ES3.GL_INFO_LOG_LENGTH, logLength)
            if (logLength.get(0) > 0) {
                val log = ByteBuffer.allocate(logLength.get(0))
                glGetShaderInfoLog(shader, logLength.get(), logLength, log)
                throw RuntimeException(String(log.array()))
            }
        }
        return@run shader
    }
}