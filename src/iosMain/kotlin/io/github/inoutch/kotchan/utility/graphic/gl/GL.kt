package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.utility.type.*
import kotlinx.cinterop.*
import platform.GLKit.*
import platform.gles3.*
import platform.glescommon.*

@ExperimentalUnsignedTypes
actual class GL {
    private var defaultFrameBufferId: Int = 0

    init {
        memScoped {
            val buffer = alloc<GLintVar>()
            glGetIntegerv(GL_FRAMEBUFFER_BINDING, buffer.ptr)
            defaultFrameBufferId = buffer.value
        }
        checkError("GL init")
    }

    actual fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
        glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).convert())
    }

    actual fun clearDepth(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
        glClear((GL_DEPTH_BUFFER_BIT).convert())
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
            return GLVBO(buffer.value.toInt())
        }
    }

    actual fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray) {
        if (data.size == 0) {
            return
        }
        glBindBuffer(GL_ARRAY_BUFFER, vbo.id.toUInt())
        glBufferSubData(GL_ARRAY_BUFFER, (offset * 4).toLong(), (data.size * 4).toLong(), data.refTo(0))
    }

    actual fun deleteVBO(vboId: Int) {
        glDeleteBuffers(1, arrayOf(vboId).toIntArray().toUIntArray().refTo(0))
    }

    actual fun bindVBO(vboId: Int) {
        glBindBuffer(GL_ARRAY_BUFFER, vboId.toUInt())
    }

    actual fun vertexPointer(location: GLAttribLocation, dimension: Int, stride: Int, offset: Int) {
        glEnableVertexAttribArray(location.value.toUInt())
        glVertexAttribPointer(
                location.value.toUInt(), dimension, GL_FLOAT,
                GL_FALSE.toByte().toUByte(), stride * 4, (offset * 4L).toCPointer<CPointed>())
    }

    actual fun disableVertexPointer(location: GLAttribLocation) {
        glDisableVertexAttribArray(location.value.toUInt())
    }

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int {
        val vertexShader = compileShader(GL_VERTEX_SHADER, "#version 300 es\n$vertexShaderText")
        val fragmentShader = compileShader(GL_FRAGMENT_SHADER, "#version 300 es\n$fragmentShaderText")
        val program = glCreateProgram()
        glAttachShader(program, vertexShader)
        glAttachShader(program, fragmentShader)

        GLAttribLocation.values().forEach {
            glEnableVertexAttribArray(it.value.toUInt())
            glBindAttribLocation(program, it.value.toUInt(), it.locationName)
        }
        glLinkProgram(program)
        glValidateProgram(program)

        checkError("LinkProgram")
        if (vertexShader.toInt() != 0) {
            glDetachShader(program, vertexShader)
            glDeleteShader(vertexShader)
        }
        if (fragmentShader.toInt() != 0) {
            glDetachShader(program, fragmentShader)
            glDeleteShader(fragmentShader)
        }
        return program.toInt()
    }

    actual fun deleteShaderProgram(shaderProgram: GLShader) {
        glDeleteProgram(shaderProgram.id.toUInt())
    }

    actual fun useProgram(shaderProgramId: Int) {
        glUseProgram(shaderProgramId.toUInt())
    }

    actual fun bindAttributeLocation(shaderProgram: GLShader, attributeLocation: GLAttribLocation, name: String) {
        glBindAttribLocation(shaderProgram.id.toUInt(), attributeLocation.value.toUInt(), name)
    }

    // getter
    actual fun getUniform(shaderProgram: GLShader, name: String): Int {
        return glGetUniformLocation(shaderProgram.id.toUInt(), name)
    }

    actual fun getUniform(shaderProgramId: Int, name: String): Int {
        return glGetUniformLocation(shaderProgramId.toUInt(), name)
    }

    actual fun getAttribLocation(shaderProgram: GLShader, name: String): Int {
        return glGetAttribLocation(shaderProgram.id.toUInt(), name)
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
        glUniformMatrix4fv(location, count, transpose.toByte().toUByte(), matrix.flatten().refTo(0))
    }

    // blend
    actual fun enableBlend() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

    actual fun blendFunc(sfactor: GLFactor, dfactor: GLFactor) {
        glBlendFunc(getFactor(sfactor).toUInt(), getFactor(dfactor).toUInt())
    }

    // depth
    actual fun enableDepth() {
        glDepthMask(GL_TRUE.toUByte())
        glEnable(GL_DEPTH_TEST)
    }

    actual fun disableDepth() {
        glDisable(GL_DEPTH_TEST)
    }

    // FBO
    actual fun bindRenderBuffer(renderBuffer: GLRenderbuffer) {
        glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer.id.toUInt())
        checkError("BindRenderBuffer")
    }

    actual fun bindFrameBuffer(frameBuffer: GLFramebuffer) {
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer.id.toUInt())
        checkError("BindFrameBuffer")
    }

    actual fun createRenderBuffer(): GLRenderbuffer {
        memScoped {
            val buffer = alloc<GLuintVar>()
            glGenRenderbuffers(1, buffer.ptr)
            checkError("CreateRenderBuffer")
            return GLRenderbuffer(buffer.value.toInt())
        }
    }

    actual fun createFrameBuffer(): GLFramebuffer {
        memScoped {
            val buffer = alloc<GLuintVar>()
            glGenFramebuffers(1, buffer.ptr)
            checkError("CreateFrameBuffer")
            return GLFramebuffer(buffer.value.toInt())
        }
    }

    actual fun frameBufferTexture(attachType: GLFramebufferAttachType, texture: GLTexture) {
        glFramebufferTexture2D(GL_FRAMEBUFFER, getFrameBufferAttachType(attachType).toUInt(), GL_TEXTURE_2D, texture.id.toUInt(), 0)
    }

    actual fun bindDefaultFrameBuffer() {
        bindFrameBuffer(GLFramebuffer(defaultFrameBufferId))
        checkError("BindDefaultFrameBuffer")
    }

    actual fun renderbufferStorage(internalFormat: GLInternalFormat, width: Int, height: Int) {
        glRenderbufferStorage(GL_RENDERBUFFER, getInternalFormat(internalFormat).toUInt(), width, height)
        checkError("RenderbufferStorage")
    }

    actual fun attachRenderBuffer(frameBuffer: GLFramebuffer, attachType: GLFramebufferAttachType, renderBuffer: GLRenderbuffer) {
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, getFrameBufferAttachType(attachType).toUInt(), GL_RENDERBUFFER, renderBuffer.id.toUInt())
        checkError("AttachRenderBuffer")
    }

    actual fun attachTexture2d(textureId: Int, attachType: GLFramebufferAttachType) {
        glFramebufferTexture2D(GL_FRAMEBUFFER, getFrameBufferAttachType(attachType).toUInt(), GL_TEXTURE_2D, textureId.toUInt(), 0)
        checkError("AttachTexture2d")
    }

    actual fun deleteRenderBuffer(id: Int) {
        glDeleteRenderbuffers(1, arrayOf(id).toIntArray().toUIntArray().refTo(0))
    }

    actual fun deleteFrameBuffer(id: Int) {
        glDeleteFramebuffers(1, arrayOf(id).toIntArray().toUIntArray().refTo(0))
    }

    actual fun checkFrameBufferStatus(): Int {
        return glCheckFramebufferStatus(GL_FRAMEBUFFER).toInt()
    }

    // texture
    actual fun enableTexture() {
        glEnable(GL_TEXTURE_2D)
    }

    actual fun disableTexture() {
        glDisable(GL_TEXTURE_2D)
    }

    actual fun activeTexture(index: Int) {
        glActiveTexture(GL_TEXTURE0.toUInt() + index.toUInt())
    }

    actual fun useTexture(texture: GLTexture?) {
        glBindTexture(GL_TEXTURE_2D, texture?.id?.toUInt() ?: 0.toUInt())
    }

    actual fun loadTexture(filepath: String): GLTexture? {
        val textureInfo = GLKTextureLoader.textureWithContentsOfFile(filepath, null, null) ?: return null
        return GLTexture(textureInfo.name.toInt(), textureInfo.width.toInt(), textureInfo.height.toInt())
    }

    actual fun deleteTexture(textureId: Int) {
        glDeleteTextures(1, arrayOf(textureId).toIntArray().toUIntArray().refTo(0))
    }

    actual fun createTexture(colors: List<Vector4>, size: Point): GLTexture? {
        // unsupported
        return null
    }

    actual fun createTexture2d(width: Int, height: Int, internalFormat: GLInternalFormat, format: GLFormat): GLTexture {
        memScoped {
            val buffer = alloc<GLuintVar>()
            glGenTextures(1, buffer.ptr)
            glBindTexture(GL_TEXTURE_2D, buffer.value)
            glTexImage2D(GL_TEXTURE_2D, 0, getInternalFormat(internalFormat), width, height,
                    0, getFormat(format).toUInt(), GL_UNSIGNED_BYTE, null)
            checkError("CreateTexture2d")
            return GLTexture(buffer.value.toInt(), width, height)
        }
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

    actual fun getError(): Int {
        return glGetError().toInt()
    }

    private fun compileShader(type: Int, text: String) = memScoped {
        val shader = glCreateShader(type.toUInt())

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
        val error = glGetError().toInt()
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

    private fun getFrameBufferAttachType(attachType: GLFramebufferAttachType) = when (attachType) {
        GLFramebufferAttachType.COLOR0 -> GL_COLOR_ATTACHMENT0
        GLFramebufferAttachType.COLOR1 -> GL_COLOR_ATTACHMENT1
        GLFramebufferAttachType.COLOR2 -> GL_COLOR_ATTACHMENT2
        GLFramebufferAttachType.DEPTH -> GL_DEPTH_ATTACHMENT
        GLFramebufferAttachType.STENCIL -> GL_STENCIL_ATTACHMENT
    }

    private fun getInternalFormat(format: GLInternalFormat) = when (format) {
        GLInternalFormat.RGBA8 -> GL_RGBA8
        GLInternalFormat.DEPTH -> GL_DEPTH_COMPONENT
        GLInternalFormat.DEPTH16 -> GL_DEPTH_COMPONENT16
    }

    private fun getFormat(format: GLFormat) = when (format) {
        GLFormat.RGBA -> GL_RGBA
    }

    private fun getFactor(factor: GLFactor) = when (factor) {
        GLFactor.GL_ZERO -> GL_ZERO
        GLFactor.GL_ONE -> GL_ONE
        GLFactor.GL_SRC_COLOR -> GL_SRC_COLOR
        GLFactor.GL_ONE_MINUS_SRC_COLOR -> GL_ONE_MINUS_SRC_COLOR
        GLFactor.GL_DST_COLOR -> GL_DST_COLOR
        GLFactor.GL_ONE_MINUS_DST_COLOR -> GL_ONE_MINUS_DST_COLOR
        GLFactor.GL_SRC_ALPHA -> GL_SRC_ALPHA
        GLFactor.GL_ONE_MINUS_SRC_ALPHA -> GL_ONE_MINUS_SRC_ALPHA
        GLFactor.GL_DST_ALPHA -> GL_DST_ALPHA
        GLFactor.GL_ONE_MINUS_DST_ALPHA -> GL_ONE_MINUS_DST_ALPHA
        GLFactor.GL_CONSTANT_COLOR -> GL_CONSTANT_COLOR
        GLFactor.GL_ONE_MINUS_CONSTANT_COLOR -> GL_ONE_MINUS_CONSTANT_COLOR
        GLFactor.GL_CONSTANT_ALPHA -> GL_CONSTANT_ALPHA
        GLFactor.GL_ONE_MINUS_CONSTANT_ALPHA -> GL_ONE_MINUS_CONSTANT_ALPHA
    }
}
