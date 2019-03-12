package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

actual class GL actual constructor() {
    actual fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
    }

    actual fun clearDepth(red: Float, green: Float, blue: Float, alpha: Float) {
    }

    actual fun viewPort(x: Int, y: Int, width: Int, height: Int) {
    }

    actual fun drawTriangleArrays(first: Int, count: Int) {
    }

    actual fun createVBO(size: Int): GLVBO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun createVBO(data: FloatArray): GLVBO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray) {
    }

    actual fun deleteVBO(vboId: Int) {
    }

    actual fun bindVBO(vboId: Int) {
    }

    actual fun vertexPointer(location: GLAttribLocation, dimension: Int, stride: Int, offset: Int) {
    }

    actual fun disableVertexPointer(location: GLAttribLocation) {
    }

    actual fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun deleteShaderProgram(shaderProgram: GLShaderProgram) {
    }

    actual fun useProgram(shaderProgramId: Int) {
    }

    actual fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String) {
    }

    actual fun getUniform(shaderProgram: GLShaderProgram, name: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun getUniform(shaderProgramId: Int, name: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun getAttribLocation(shaderProgram: GLShaderProgram, name: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun uniform1i(location: Int, v0: Int) {
    }

    actual fun uniform1f(location: Int, v0: Float) {
    }

    actual fun uniform3f(location: Int, v: Vector3) {
    }

    actual fun uniform4f(location: Int, v: Vector4) {
    }

    actual fun uniformMatrix4fv(location: Int, count: Int, transpose: Boolean, matrix: Matrix4) {
    }

    actual fun enableBlend() {
    }

    actual fun blendFunc(sfactor: GLFactor, dfactor: GLFactor) {
    }

    actual fun enableDepth() {
    }

    actual fun disableDepth() {
    }

    actual fun activeTexture(index: Int) {
    }

    actual fun useTexture(texture: GLTexture?) {
    }

    actual fun loadTexture(filepath: String): GLTexture? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun deleteTexture(textureId: Int) {
    }

    actual fun filterTexture(type: GLFilterType) {
    }

    actual fun createTexture2d(width: Int, height: Int, internalFormat: GLInternalFormat, format: GLFormat): GLTexture {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun createRenderBuffer(): GLRenderbuffer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun createFrameBuffer(): GLFramebuffer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun bindRenderBuffer(renderBuffer: GLRenderbuffer) {
    }

    actual fun bindFrameBuffer(frameBuffer: GLFramebuffer) {
    }

    actual fun bindDefaultFrameBuffer() {
    }

    actual fun renderbufferStorage(internalFormat: GLInternalFormat, width: Int, height: Int) {
    }

    actual fun attachRenderBuffer(frameBuffer: GLFramebuffer, attachType: GLFrameBufferAttachType, renderBuffer: GLRenderbuffer) {
    }

    actual fun attachTexture2d(textureId: Int, attachType: GLFrameBufferAttachType) {
    }

    actual fun deleteRenderBuffer(id: Int) {
    }

    actual fun deleteFrameBuffer(id: Int) {
    }

    actual fun frameBufferTexture(attachType: GLFrameBufferAttachType, texture: GLTexture) {
    }

    actual fun checkFrameBufferStatus(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
