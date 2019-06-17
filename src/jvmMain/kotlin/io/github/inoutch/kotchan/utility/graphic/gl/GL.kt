package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.utility.graphic.UnsupportedPlatformError
import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

actual class GL actual constructor() {
    actual fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        throw UnsupportedPlatformError()
    }

    actual fun clearDepth(red: Float, green: Float, blue: Float, alpha: Float) {
        throw UnsupportedPlatformError()
    }

    actual fun viewPort(x: Int, y: Int, width: Int, height: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun drawTriangleArrays(first: Int, count: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun createVBO(size: Int): GLVBO {
        throw UnsupportedPlatformError()
    }

    actual fun createVBO(data: FloatArray): GLVBO {
        throw UnsupportedPlatformError()
    }

    actual fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray) {
        throw UnsupportedPlatformError()
    }

    actual fun deleteVBO(vboId: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun bindVBO(vboId: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun vertexPointer(location: GLAttribLocation, dimension: Int, stride: Int, offset: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun disableVertexPointer(location: GLAttribLocation) {
        throw UnsupportedPlatformError()
    }

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int {
        throw UnsupportedPlatformError()
    }

    actual fun deleteShaderProgram(shaderProgram: GLShader) {
        throw UnsupportedPlatformError()
    }

    actual fun useProgram(shaderProgramId: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun bindAttributeLocation(shaderProgram: GLShader, attributeLocation: GLAttribLocation, name: String) {
        throw UnsupportedPlatformError()
    }

    actual fun getUniform(shaderProgram: GLShader, name: String): Int {
        throw UnsupportedPlatformError()
    }

    actual fun getUniform(shaderProgramId: Int, name: String): Int {
        throw UnsupportedPlatformError()
    }

    actual fun getAttribLocation(shaderProgram: GLShader, name: String): Int {
        throw UnsupportedPlatformError()
    }

    actual fun uniform1i(location: Int, v0: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun uniform1f(location: Int, v0: Float) {
        throw UnsupportedPlatformError()
    }

    actual fun uniform3f(location: Int, v: Vector3) {
        throw UnsupportedPlatformError()
    }

    actual fun uniform4f(location: Int, v: Vector4) {
        throw UnsupportedPlatformError()
    }

    actual fun uniformMatrix4fv(location: Int, count: Int, transpose: Boolean, matrix: Matrix4) {
        throw UnsupportedPlatformError()
    }

    actual fun enableBlend() {
        throw UnsupportedPlatformError()
    }

    actual fun blendFunc(sfactor: GLFactor, dfactor: GLFactor) {
        throw UnsupportedPlatformError()
    }

    actual fun enableDepth() {
        throw UnsupportedPlatformError()
    }

    actual fun disableDepth() {
        throw UnsupportedPlatformError()
    }

    actual fun enableTexture() {
        throw UnsupportedPlatformError()
    }

    actual fun disableTexture() {
        throw UnsupportedPlatformError()
    }

    actual fun activeTexture(index: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun useTexture(texture: GLTexture?) {
        throw UnsupportedPlatformError()
    }

    actual fun loadTexture(filepath: String): GLTexture? {
        throw UnsupportedPlatformError()
    }

    actual fun deleteTexture(textureId: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun filterTexture(type: GLFilterType) {
        throw UnsupportedPlatformError()
    }

    actual fun createRenderBuffer(): GLRenderbuffer {
        throw UnsupportedPlatformError()
    }

    actual fun createFrameBuffer(): GLFramebuffer {
        throw UnsupportedPlatformError()
    }

    actual fun bindRenderBuffer(renderBuffer: GLRenderbuffer) {
        throw UnsupportedPlatformError()
    }

    actual fun bindFrameBuffer(frameBuffer: GLFramebuffer) {
        throw UnsupportedPlatformError()
    }

    actual fun bindDefaultFrameBuffer() {
        throw UnsupportedPlatformError()
    }

    actual fun renderbufferStorage(internalFormat: GLInternalFormat, width: Int, height: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun attachRenderBuffer(frameBuffer: GLFramebuffer, attachType: GLFramebufferAttachType, renderBuffer: GLRenderbuffer) {
        throw UnsupportedPlatformError()
    }

    actual fun attachTexture2d(textureId: Int, attachType: GLFramebufferAttachType) {
        throw UnsupportedPlatformError()
    }

    actual fun deleteRenderBuffer(id: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun deleteFrameBuffer(id: Int) {
        throw UnsupportedPlatformError()
    }

    actual fun frameBufferTexture(attachType: GLFramebufferAttachType, texture: GLTexture) {
        throw UnsupportedPlatformError()
    }

    actual fun checkFrameBufferStatus(): Int {
        throw UnsupportedPlatformError()
    }

    actual fun createTexture2d(width: Int, height: Int, internalFormat: GLInternalFormat, format: GLFormat): GLTexture {
        throw UnsupportedPlatformError()
    }

    actual fun getError(): Int = throw UnsupportedPlatformError()
}
