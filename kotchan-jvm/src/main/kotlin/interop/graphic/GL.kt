package interop.graphic

import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.GL4ES3
import com.jogamp.opengl.util.texture.TextureIO
import utility.type.Matrix4
import java.io.File
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.awt.image.BufferedImage
import java.io.IOException
import com.jogamp.opengl.util.awt.ImageUtil
import com.jogamp.opengl.util.texture.awt.AWTTextureIO
import utility.type.Vector3
import utility.type.Vector4
import javax.imageio.ImageIO


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

    actual fun drawTriangleArrays(first: Int, count: Int) {
        gl.glDrawArrays(GL4ES3.GL_TRIANGLES, first, count)
    }

    actual fun createVBO(data: FloatArray): GLVBO {
        val vertexBuffer = FloatBuffer.wrap(data)
        val id = arrayOf(0).toIntArray()
        gl.glGenBuffers(1, id, 0)
        gl.glBindBuffer(GL4ES3.GL_ARRAY_BUFFER, id.first())
        gl.glBufferData(GL4ES3.GL_ARRAY_BUFFER, (data.size * 4).toLong(), vertexBuffer, GL4ES3.GL_DYNAMIC_DRAW)
        return GLVBO(id.first())
    }

    actual fun createVBO(size: Int): GLVBO {
        return createVBO(FloatArray(size, { 0.0f }))
    }

    actual fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray) {
        val vertexBuffer = FloatBuffer.wrap(data)
        gl.glBindBuffer(GL4ES3.GL_ARRAY_BUFFER, vbo.id)
        gl.glBufferSubData(GL4ES3.GL_ARRAY_BUFFER, (offset * 4).toLong(), (data.size * 4).toLong(), vertexBuffer)
    }

    actual fun destroyVBO(vboId: Int) {
        gl.glDeleteBuffers(1, IntBuffer.wrap(IntArray(1, { vboId })))
    }

    actual fun bindVBO(vboId: Int) {
        gl.glBindBuffer(GL4ES3.GL_ARRAY_BUFFER, vboId)
    }

    actual fun vertexPointer(location: GLAttribLocation, dimension: Int, stride: Int, offset: Int) {
        gl.glEnableVertexAttribArray(location.value)
        gl.glVertexAttribPointer(location.value, dimension, GL4ES3.GL_FLOAT, false, stride * 4, offset.toLong() * 4)
    }

    actual fun disableVertexPointer(location: GLAttribLocation) {
        gl.glDisableVertexAttribArray(location.value)
    }

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int {
        return gl.run {
            val vertexShader = compileShader(GL4ES3.GL_VERTEX_SHADER, "#version 410\n$vertexShaderText")
            val fragmentShader = compileShader(GL4ES3.GL_FRAGMENT_SHADER, "#version 410\n$fragmentShaderText")

            val shaderProgram = glCreateProgram()
            glAttachShader(shaderProgram, vertexShader)
            glAttachShader(shaderProgram, fragmentShader)

            GLAttribLocation.values().forEach {
                glBindAttribLocation(shaderProgram, it.value, it.locationName)
            }
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
            return@run shaderProgram
        }
    }

    actual fun deleteShaderProgram(shaderProgram: GLShaderProgram) {
        gl.glDeleteProgram(shaderProgram.id)
    }

    actual fun useProgram(shaderProgramId: Int) {
        gl.glUseProgram(shaderProgramId)
    }

    actual fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String) {
        gl.glBindAttribLocation(shaderProgram.id, attributeLocation.value, name)
    }

    // getter
    actual fun getUniform(shaderProgram: GLShaderProgram, name: String): Int {
        return gl.glGetUniformLocation(shaderProgram.id, name)
    }

    actual fun getUniform(shaderProgramId: Int, name: String): Int {
        return gl.glGetUniformLocation(shaderProgramId, name)
    }

    actual fun getAttribLocation(shaderProgram: GLShaderProgram, name: String): Int {
        return gl.glGetAttribLocation(shaderProgram.id, name)
    }

    // uniforms
    actual fun uniform1i(location: Int, v0: Int) {
        gl.glUniform1i(location, v0)
    }

    actual fun uniform1f(location: Int, v0: Float) {
        gl.glUniform1f(location, v0)
    }

    actual fun uniform3f(location: Int, v: Vector3) {
        gl.glUniform3f(location, v.x, v.y, v.z)
    }

    actual fun uniform4f(location: Int, v: Vector4) {
        gl.glUniform4f(location, v.x, v.y, v.z, v.w)
    }

    actual fun uniformMatrix4fv(location: Int, count: Int, transpose: Boolean, matrix: Matrix4) {
        val matrixBuffer = Buffers.newDirectFloatBuffer(matrix.flatten())
        gl.glUniformMatrix4fv(location, count, transpose, matrixBuffer)
    }

    // texture
    actual fun activeTexture(index: Int) {
        gl.glActiveTexture(GL4ES3.GL_TEXTURE0 + index)
    }

    actual fun useTexture(texture: GLTexture?) {
        gl.glBindTexture(GL4ES3.GL_TEXTURE_2D, texture?.id ?: 0)
    }

    actual fun loadTexture(filepath: String): GLTexture? {
        return try {
            val buffer = loadBuffer(filepath, false) ?: return null
            val textureData = AWTTextureIO.newTextureData(gl.glProfile, buffer, false)
            val texture = TextureIO.newTexture(textureData)
            GLTexture(texture.getTextureObject(gl), texture.width, texture.height)
        } catch (e: FileNotFoundException) {
            null
        }
    }

    actual fun destroyTexture(textureId: Int) {
        gl.glDeleteTextures(1, IntBuffer.wrap(IntArray(1, { textureId })))
    }

    actual fun filterTexture(type: GLFilterType) {
        when (type) {
            GLFilterType.Nearest -> {
                gl.glTexParameteri(GL4ES3.GL_TEXTURE_2D, GL4ES3.GL_TEXTURE_MIN_FILTER, GL4ES3.GL_NEAREST)
                gl.glTexParameteri(GL4ES3.GL_TEXTURE_2D, GL4ES3.GL_TEXTURE_MAG_FILTER, GL4ES3.GL_NEAREST)
            }
            GLFilterType.Linear -> {
                gl.glTexParameteri(GL4ES3.GL_TEXTURE_2D, GL4ES3.GL_TEXTURE_MIN_FILTER, GL4ES3.GL_LINEAR)
                gl.glTexParameteri(GL4ES3.GL_TEXTURE_2D, GL4ES3.GL_TEXTURE_MAG_FILTER, GL4ES3.GL_LINEAR)
            }
        }
    }

    // blend
    actual fun enableBlend() {
        gl.glEnable(GL4ES3.GL_BLEND)
        gl.glBlendFunc(GL4ES3.GL_SRC_ALPHA, GL4ES3.GL_ONE_MINUS_SRC_ALPHA)
    }

    // private
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

    private fun loadBuffer(filepath: String, flipVertical: Boolean): BufferedImage? {
        return try {
            val file = File(filepath)
            val image = ImageIO.read(file)
            if (image != null && flipVertical) {
                ImageUtil.flipImageVertically(image)
            }
            image
        } catch (e: IOException) {
            null
        }
    }
}