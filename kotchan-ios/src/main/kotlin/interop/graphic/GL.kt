package interop.graphic

import kotlinx.cinterop.*
import platform.glescommon.*
import platform.gles3.*
import platform.GLKit.*

import utility.type.*

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
        return createVBO(FloatArray(size, { 0.0f }))
    }

    actual fun createVBO(data: FloatArray): GLVBO {
        memScoped {
            val buffer = alloc<GLuintVar>()
            glGenBuffers(1, buffer.ptr)
            glBindBuffer(GL_ARRAY_BUFFER, buffer.value)
            glBufferData(GL_ARRAY_BUFFER, (data.size * 4).toLong(), data.refTo(0), GL_DYNAMIC_DRAW)
            return GLVBO(buffer.value)
        }
    }

    actual fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray) {
        if (data.size == 0) {
            return
        }
        glBindBuffer(GL_ARRAY_BUFFER, vbo.id)
        glBufferSubData(GL_ARRAY_BUFFER, (offset * 4).toLong(), (data.size * 4).toLong(), data.refTo(0))
    }

    actual fun destroyVBO(vboId: Int) {
        glDeleteBuffers(1, arrayOf(vboId).toIntArray().refTo(0))
    }

    actual fun bindVBO(vboId: Int) {
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
    }

    actual fun vertexPointer(location: GLAttribLocation, dimension: Int, stride: Int, offset: Int) {
        glEnableVertexAttribArray(location.value)
        glVertexAttribPointer(
                location.value, dimension, GL_FLOAT,
                GL_FALSE.narrow(), stride * 4, (offset * 4L).toCPointer<CPointed>())
    }

    actual fun disableVertexPointer(location: GLAttribLocation) {
        glDisableVertexAttribArray(location.value)
    }

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int {
        val vertexShader = compileShader(GL_VERTEX_SHADER, "#version 300 es\n$vertexShaderText")
        val fragmentShader = compileShader(GL_FRAGMENT_SHADER, "#version 300 es\n$fragmentShaderText")
        val program = glCreateProgram()
        glAttachShader(program, vertexShader)
        glAttachShader(program, fragmentShader)

        GLAttribLocation.values().forEach {
            glEnableVertexAttribArray(it.value)
            glBindAttribLocation(program, it.value, it.locationName)
        }
        glLinkProgram(program)
        glValidateProgram(program)

        checkError("LinkProgram")
        if (vertexShader != 0) {
            glDetachShader(program, vertexShader)
            glDeleteShader(vertexShader)
        }
        if (fragmentShader != 0) {
            glDetachShader(program, fragmentShader)
            glDeleteShader(fragmentShader)
        }
        return program
    }

    actual fun deleteShaderProgram(shaderProgram: GLShaderProgram) {
        glDeleteProgram(shaderProgram.id)
    }

    actual fun useProgram(shaderProgramId: Int) {
        glUseProgram(shaderProgramId)
    }

    actual fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String) {
        glBindAttribLocation(shaderProgram.id, attributeLocation.value, name)
    }

    // getter
    actual fun getUniform(shaderProgram: GLShaderProgram, name: String): Int {
        return glGetUniformLocation(shaderProgram.id, name)
    }

    actual fun getUniform(shaderProgramId: Int, name: String): Int {
        return glGetUniformLocation(shaderProgramId, name)
    }

    actual fun getAttribLocation(shaderProgram: GLShaderProgram, name: String): Int {
        return glGetAttribLocation(shaderProgram.id, name)
    }

    // uniform
    actual fun uniform1i(location: Int, v0: Int) {
        glUniform1i(location, v0)
    }

    actual fun uniform1f(location: Int, v0: Float) {
        glUniform1f(location, v0)
    }

    actual fun uniform3f(location: Int, v: Vector3) {
        glUniform3f(location, v.x, v.y, v.z)
    }

    actual fun uniform4f(location: Int, v: Vector4) {
        glUniform4f(location, v.x, v.y, v.z, v.w)
    }

    actual fun uniformMatrix4fv(location: Int, count: Int, transpose: Boolean, matrix: Matrix4) {
        glUniformMatrix4fv(location, count, transpose.toByte(), matrix.flatten().refTo(0))
    }

    // blend
    actual fun enableBlend() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

    // depth
    actual fun enableDepth() {
        glDepthMask(GL_TRUE.toByte())
        glEnable(GL_DEPTH_TEST)
    }

    actual fun disableDepth() {
        glDisable(GL_DEPTH_TEST)
    }

    // texture
    actual fun activeTexture(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
    }

    actual fun useTexture(texture: GLTexture?) {
        glBindTexture(GL_TEXTURE_2D, texture?.id ?: 0)
    }

    actual fun loadTexture(filepath: String): GLTexture? {
        val textureInfo = GLKTextureLoader.textureWithContentsOfFile(filepath, null, null) ?: return null
        return GLTexture(textureInfo.name, textureInfo.width, textureInfo.height)
    }

    actual fun destroyTexture(textureId: Int) {
        glDeleteTextures(1, arrayOf(textureId).toIntArray().refTo(0))
    }

    actual fun filterTexture(type: GLFilterType) {
        when (type) {
            GLFilterType.Nearest -> {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            }
            GLFilterType.Linear -> {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            }
        }
    }

    private fun compileShader(type: Int, text: String) = memScoped {
        val shader = glCreateShader(type)
        checkError("CreateShader")

        glShaderSource(shader, 1, cValuesOf(text.cstr.getPointer(memScope)), null)
        glCompileShader(shader)

        val statusVar = alloc<GLintVar>()
        glGetShaderiv(shader, GL_COMPILE_STATUS, statusVar.ptr)
        if (statusVar.value != GL_TRUE) {
            val logBuffer = allocArray<ByteVar>(512)
            glGetShaderInfoLog(shader, 512, null, logBuffer)
            throw Error("Shader compilation failed: ${logBuffer.toKString()}")
        }

        checkError("CompileShader")
        shader
    }

    private fun checkError(tag: String) {
        val error = glGetError()
        if (error != 0) {
            val errorString = when (error) {
                GL_INVALID_ENUM -> "GL_INVALID_ENUM"
                GL_INVALID_VALUE -> "GL_INVALID_VALUE"
                GL_INVALID_OPERATION -> "GL_INVALID_OPERATION"
                GL_INVALID_FRAMEBUFFER_OPERATION -> "GL_INVALID_FRAMEBUFFER_OPERATION"
                GL_OUT_OF_MEMORY -> "GL_OUT_OF_MEMORY"
                else -> "unknown"
            }
            throw Error("GL error: 0x${error.toString(16)} [tag:$tag] ($errorString)")
        }
    }
}
