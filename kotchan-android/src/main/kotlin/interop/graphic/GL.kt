package interop.graphic

import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.renderscript.ScriptGroup
import com.example.app.MainActivity

import utility.type.*
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.io.File;



actual class GL {
    actual fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glClearColor(red, green, blue, alpha)
    }

    actual fun viewPort(x: Int, y: Int, width: Int, height: Int) {
        GLES30.glViewport(x, y, width, height)
    }

    actual fun drawTriangleArrays(first: Int, count: Int) {
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, first, count)
    }

    actual fun createVBO(size: Int): GLVBO {
        return createVBO(FloatArray(size, { 0.0f }))
    }

    actual fun createVBO(data: FloatArray): GLVBO {
        val vertexBuffer = FloatBuffer.wrap(data)
        val id = arrayOf(0).toIntArray()
        GLES30.glGenBuffers(1, id, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, id.first())
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, data.size * 4, vertexBuffer, GLES30.GL_DYNAMIC_DRAW)
        return GLVBO(id.first())
    }


    actual fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray) {
        val vertexBuffer = FloatBuffer.wrap(data)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo.id)
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, offset * 4, data.size * 4, vertexBuffer)
    }

    actual fun destroyVBO(vboId: Int) {
        GLES30.glDeleteBuffers(1, IntBuffer.wrap(IntArray(1, { vboId })))
    }

    actual fun bindVBO(vboId: Int) {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboId)
    }
    actual fun vertexPointer(location: GLAttribLocation, dimension: Int, stride: Int, offset: Int) {
        GLES30.glEnableVertexAttribArray(location.value)
        GLES30.glVertexAttribPointer(location.value, dimension, GLES30.GL_FLOAT, false, stride * 4, offset * 4)
    }
    actual fun disableVertexPointer(location: GLAttribLocation) {
        GLES30.glDisableVertexAttribArray(location.value)
    }

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int {
        val vertexShader = compileShader(GLES30.GL_VERTEX_SHADER, "#version 300 es\n$vertexShaderText")
        val fragmentShader = compileShader(GLES30.GL_FRAGMENT_SHADER, "#version 300 es\n$fragmentShaderText")

        val shaderProgram = GLES30.glCreateProgram()
        GLES30.glAttachShader(shaderProgram, vertexShader)
        GLES30.glAttachShader(shaderProgram, fragmentShader)

        GLAttribLocation.values().forEach {
            GLES30.glBindAttribLocation(shaderProgram, it.value, it.locationName)
        }
        GLES30.glLinkProgram(shaderProgram)
        GLES30.glValidateProgram(shaderProgram)

        if (vertexShader != 0) {
            GLES30.glDetachShader(shaderProgram, vertexShader)
            GLES30.glDeleteShader(vertexShader)
        }
        if (fragmentShader != 0) {
            GLES30.glDetachShader(shaderProgram, fragmentShader)
            GLES30.glDeleteShader(fragmentShader)
        }
        return shaderProgram
    }

    actual fun deleteShaderProgram(shaderProgram: GLShaderProgram) {
        GLES30.glDeleteProgram(shaderProgram.id)
    }

    actual fun useProgram(shaderProgramId: Int) {
        GLES30.glUseProgram(shaderProgramId)
    }

    actual fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String) {
        GLES30.glBindAttribLocation(shaderProgram.id, attributeLocation.value, name)
    }

    // getter
    actual fun getUniform(shaderProgram: GLShaderProgram, name: String): Int {
        return GLES30.glGetUniformLocation(shaderProgram.id, name)
    }

    actual fun getUniform(shaderProgramId: Int, name: String): Int {
        return GLES30.glGetUniformLocation(shaderProgramId, name)
    }

    actual fun getAttribLocation(shaderProgram: GLShaderProgram, name: String): Int {
        return GLES30.glGetAttribLocation(shaderProgram.id, name)
    }

    // uniforms
    actual fun uniform1i(location: Int, v0: Int) {
        GLES30.glUniform1i(location, v0)
    }

    actual fun uniform1f(location: Int, v0: Float) {
        GLES30.glUniform1f(location, v0)
    }

    actual fun uniform3f(location: Int, v: Vector3) {
        GLES30.glUniform3f(location, v.x, v.y, v.z)
    }

    actual fun uniform4f(location: Int, v: Vector4) {
        GLES30.glUniform4f(location, v.x, v.y, v.z, v.w)
    }

    actual fun uniformMatrix4fv(location: Int, count: Int, transpose: Boolean, matrix: Matrix4) {
        val matrixBuffer = matrix.flatten()
        GLES30.glUniformMatrix4fv(location, count, transpose, FloatBuffer.wrap(matrixBuffer))
    }

    // texture

    actual fun activeTexture(index: Int) {
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + index)
    }

    actual fun useTexture(texture: GLTexture?) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture?.id ?: 0)
    }

    actual fun loadTexture(filepath: String): GLTexture? {
        var inputstream = getInputStream(filepath)
        val bitmap = BitmapFactory.decodeStream(inputstream) ?: return null
        val texture = IntBuffer.allocate(1)
        GLES30.glGenTextures(1, texture)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture[0])
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        return GLTexture(texture[0], bitmap.width, bitmap.height)
    }
    private fun getInputStream(filepath: String): InputStream?{
        return if(filepath.startsWith("@assets/"))
           MainActivity.getAssets()?.open(filepath.removePrefix("@assets/"))
        else
            FileInputStream(File(filepath))

    }

    actual fun destroyTexture(textureId: Int) {
        GLES30.glDeleteTextures(1, IntBuffer.wrap(IntArray(1, { textureId })))
    }

    actual fun filterTexture(type: GLFilterType) {
        when (type) {
            GLFilterType.Nearest -> {
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST)
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST)
            }
            GLFilterType.Linear -> {
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
            }
        }
    }

    // blend
    actual fun enableBlend() {
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)
    }

    private fun compileShader(type: Int, text: String): Int {
        val shader = GLES30.glCreateShader(type)
        GLES30.glShaderSource(shader, text)
        GLES30.glCompileShader(shader)
        val status = IntBuffer.allocate(1)
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, status)
        if (status.get(0) == GLES30.GL_FALSE) {
            val logLength = IntBuffer.allocate(1)
            GLES30.glGetShaderiv(shader, GLES30.GL_INFO_LOG_LENGTH, logLength)
            if (logLength.get(0) > 0) {
                val log = ByteBuffer.allocate(logLength.get(0))
                GLES30.glGetShaderInfoLog(shader)
                throw Error(String(log.array()))
            }
        }
        return shader
    }
}
