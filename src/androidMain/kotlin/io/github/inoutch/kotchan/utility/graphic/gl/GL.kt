package io.github.inoutch.kotchan.utility.graphic.gl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import io.github.inoutch.kotchan.android.KotchanActivity
import io.github.inoutch.kotchan.utility.type.*
import java.nio.*

actual class GL {
    actual fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        GLES30.glClearColor(red, green, blue, alpha)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
    }

    actual fun clearDepth(red: Float, green: Float, blue: Float, alpha: Float) {
        GLES30.glClearColor(red, green, blue, alpha)
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT)
    }

    actual fun viewPort(x: Int, y: Int, width: Int, height: Int) {
        GLES30.glViewport(x, y, width, height)
    }

    actual fun drawTriangleArrays(first: Int, count: Int) {
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, first, count)
    }

    actual fun createVBO(size: Int): GLVBO {
        return createVBO(FloatArray(size) { 0.0f })
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

    actual fun deleteVBO(vboId: Int) {
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

    actual fun deleteShaderProgram(shaderProgram: GLShader) {
        GLES30.glDeleteProgram(shaderProgram.id)
    }

    actual fun useProgram(shaderProgramId: Int) {
        GLES30.glUseProgram(shaderProgramId)
    }

    actual fun bindAttributeLocation(shaderProgram: GLShader, attributeLocation: GLAttribLocation, name: String) {
        GLES30.glBindAttribLocation(shaderProgram.id, attributeLocation.value, name)
    }

    // getter
    actual fun getUniform(shaderProgram: GLShader, name: String): Int {
        return GLES30.glGetUniformLocation(shaderProgram.id, name)
    }

    actual fun getUniform(shaderProgramId: Int, name: String): Int {
        return GLES30.glGetUniformLocation(shaderProgramId, name)
    }

    actual fun getAttribLocation(shaderProgram: GLShader, name: String): Int {
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
    actual fun enableTexture() {
        GLES30.glEnable(GLES30.GL_TEXTURE_2D)
    }

    actual fun disableTexture() {
        GLES30.glDisable(GLES30.GL_TEXTURE_2D)
    }

    actual fun activeTexture(index: Int) {
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + index)
    }

    actual fun useTexture(texture: GLTexture?) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture?.id ?: 0)
    }

    actual fun loadTexture(filepath: String): GLTexture? {
        KotchanActivity.inputStream(filepath)?.use {
            val bitmap = BitmapFactory.decodeStream(it) ?: return null
            val texture = IntBuffer.allocate(1)
            GLES30.glGenTextures(1, texture)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture[0])
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
            return GLTexture(texture[0], bitmap.width, bitmap.height)
        }
        return null
    }

    actual fun deleteTexture(textureId: Int) {
        GLES30.glDeleteTextures(1, IntBuffer.wrap(IntArray(1) { textureId }))
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

    actual fun createTexture(colors: List<Vector4>, size: Point): GLTexture? {
        val pixels = colors
                .map { it * 255.0f }
                .map { it.x.toInt() or (it.y.toInt() shl 8) or (it.z.toInt() shl 16) or (it.z.toInt() shl 24) }
                .toIntArray()
        val bitmap = Bitmap.createBitmap(pixels, size.x, size.y, Bitmap.Config.ARGB_8888)
        val texture = IntBuffer.allocate(1)
        GLES30.glGenTextures(1, texture)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture[0])
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        return GLTexture(texture[0], bitmap.width, bitmap.height)
    }

    actual fun createTexture2d(width: Int, height: Int, internalFormat: GLInternalFormat, format: GLFormat): GLTexture {
        val id = IntBuffer.allocate(1)
        GLES30.glGenTextures(1, id)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, id.get(0))
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, getInternalFormat(internalFormat), width, height,
                0, getFormat(format), GLES30.GL_UNSIGNED_BYTE, null)
        return GLTexture(id.get(0), width, height)
    }

    // blend
    actual fun enableBlend() {
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)
    }

    actual fun blendFunc(sfactor: GLFactor, dfactor: GLFactor) {
        GLES30.glBlendFunc(getFactor(sfactor), getFactor(dfactor))
    }

    // depth
    actual fun enableDepth() {
        GLES30.glDepthMask(true)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    actual fun disableDepth() {
        GLES30.glDisable(GLES30.GL_DEPTH_TEST)
    }

    actual fun bindRenderBuffer(renderBuffer: GLRenderbuffer) {
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderBuffer.id)
    }

    actual fun bindFrameBuffer(frameBuffer: GLFramebuffer) {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer.id)
    }

    actual fun createRenderBuffer(): GLRenderbuffer {
        val id = IntBuffer.wrap(IntArray(1))
        GLES30.glGenRenderbuffers(1, id)
        return GLRenderbuffer(id[0])
    }

    actual fun createFrameBuffer(): GLFramebuffer {
        val id = IntBuffer.wrap(IntArray(1))
        GLES30.glGenFramebuffers(1, id)
        return GLFramebuffer(id[0])
    }

    actual fun frameBufferTexture(attachType: GLFramebufferAttachType, texture: GLTexture) {
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, getFrameBufferAttachType(attachType), GLES30.GL_TEXTURE_2D, texture.id, 0)
    }

    actual fun bindDefaultFrameBuffer() {
        bindFrameBuffer(GLFramebuffer(0))
    }

    actual fun renderbufferStorage(internalFormat: GLInternalFormat, width: Int, height: Int) {
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, getInternalFormat(internalFormat), width, height)
    }

    actual fun attachRenderBuffer(frameBuffer: GLFramebuffer, attachType: GLFramebufferAttachType, renderBuffer: GLRenderbuffer) {
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, getFrameBufferAttachType(attachType), GLES30.GL_RENDERBUFFER, renderBuffer.id)
    }

    actual fun attachTexture2d(textureId: Int, attachType: GLFramebufferAttachType) {
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, getFrameBufferAttachType(attachType), GLES30.GL_TEXTURE_2D, textureId, 0)
    }

    actual fun deleteRenderBuffer(id: Int) {
        GLES30.glDeleteRenderbuffers(1, IntBuffer.wrap(IntArray(1) { id }))
    }

    actual fun deleteFrameBuffer(id: Int) {
        GLES30.glDeleteFramebuffers(1, IntBuffer.wrap(IntArray(1) { id }))
    }

    actual fun checkFrameBufferStatus(): Int {
        return GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER)
    }

    actual fun getError(): Int {
        return GLES30.glGetError()
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

    private fun getFrameBufferAttachType(attachType: GLFramebufferAttachType): Int {
        return when (attachType) {
            GLFramebufferAttachType.COLOR0 -> GLES30.GL_COLOR_ATTACHMENT0
            GLFramebufferAttachType.COLOR1 -> GLES30.GL_COLOR_ATTACHMENT1
            GLFramebufferAttachType.COLOR2 -> GLES30.GL_COLOR_ATTACHMENT2
            GLFramebufferAttachType.DEPTH -> GLES30.GL_DEPTH_ATTACHMENT
            GLFramebufferAttachType.STENCIL -> GLES30.GL_STENCIL_ATTACHMENT
        }
    }

    private fun getInternalFormat(format: GLInternalFormat) = when (format) {
        GLInternalFormat.RGBA8 -> GLES30.GL_RGBA8
        GLInternalFormat.DEPTH -> GLES30.GL_DEPTH_COMPONENT
        GLInternalFormat.DEPTH16 -> GLES30.GL_DEPTH_COMPONENT16
    }

    private fun getFormat(format: GLFormat) = when (format) {
        GLFormat.RGBA -> GLES30.GL_RGBA
    }

    private fun getFactor(factor: GLFactor) = when (factor) {
        GLFactor.GL_ZERO -> GLES30.GL_ZERO
        GLFactor.GL_ONE -> GLES30.GL_ONE
        GLFactor.GL_SRC_COLOR -> GLES30.GL_SRC_COLOR
        GLFactor.GL_ONE_MINUS_SRC_COLOR -> GLES30.GL_ONE_MINUS_SRC_COLOR
        GLFactor.GL_DST_COLOR -> GLES30.GL_DST_COLOR
        GLFactor.GL_ONE_MINUS_DST_COLOR -> GLES30.GL_ONE_MINUS_DST_COLOR
        GLFactor.GL_SRC_ALPHA -> GLES30.GL_SRC_ALPHA
        GLFactor.GL_ONE_MINUS_SRC_ALPHA -> GLES30.GL_ONE_MINUS_SRC_ALPHA
        GLFactor.GL_DST_ALPHA -> GLES30.GL_DST_ALPHA
        GLFactor.GL_ONE_MINUS_DST_ALPHA -> GLES30.GL_ONE_MINUS_DST_ALPHA
        GLFactor.GL_CONSTANT_COLOR -> GLES30.GL_CONSTANT_COLOR
        GLFactor.GL_ONE_MINUS_CONSTANT_COLOR -> GLES30.GL_ONE_MINUS_CONSTANT_COLOR
        GLFactor.GL_CONSTANT_ALPHA -> GLES30.GL_CONSTANT_ALPHA
        GLFactor.GL_ONE_MINUS_CONSTANT_ALPHA -> GLES30.GL_ONE_MINUS_CONSTANT_ALPHA
    }
}
