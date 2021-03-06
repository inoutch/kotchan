package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

expect class GL() {

    fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)

    fun clearDepth(red: Float, green: Float, blue: Float, alpha: Float)

    fun viewPort(x: Int, y: Int, width: Int, height: Int)

    fun drawTriangleArrays(first: Int, count: Int)

    fun createVBO(size: Int): GLVBO

    fun createVBO(data: FloatArray): GLVBO

    fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray)

    fun deleteVBO(vboId: Int)

    fun bindVBO(vboId: Int)

    fun vertexPointer(location: GLAttribLocation, dimension: Int, stride: Int, offset: Int)

    fun disableVertexPointer(location: GLAttribLocation)

    fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int

    fun deleteShaderProgram(shaderProgram: GLShader)

    fun useProgram(shaderProgramId: Int)

    fun bindAttributeLocation(shaderProgram: GLShader, attributeLocation: GLAttribLocation, name: String)

    // getter
    fun getUniform(shaderProgram: GLShader, name: String): Int

    fun getUniform(shaderProgramId: Int, name: String): Int

    fun getAttribLocation(shaderProgram: GLShader, name: String): Int

    // uniform
    fun uniform1i(location: Int, v0: Int)

    fun uniform1f(location: Int, v0: Float)

    fun uniform3f(location: Int, v: Vector3)

    fun uniform4f(location: Int, v: Vector4)

    fun uniformMatrix4fv(location: Int, count: Int, transpose: Boolean, matrix: Matrix4)

    // blend
    fun enableBlend()

    fun blendFunc(sfactor: GLFactor, dfactor: GLFactor)

    // depth
    fun enableDepth()

    fun disableDepth()

    // texture
    fun enableTexture()

    fun disableTexture()

    fun activeTexture(index: Int)

    fun useTexture(texture: GLTexture?)

    fun loadTexture(filepath: String): GLTexture?

    fun deleteTexture(textureId: Int)

    fun filterTexture(type: GLFilterType)

    fun createTexture(colors: List<Vector4>, size: Point): GLTexture?

    fun createTexture2d(width: Int, height: Int, internalFormat: GLInternalFormat, format: GLFormat): GLTexture

    //
    fun createRenderBuffer(): GLRenderbuffer

    fun createFrameBuffer(): GLFramebuffer

    fun bindRenderBuffer(renderBuffer: GLRenderbuffer)

    fun bindFrameBuffer(frameBuffer: GLFramebuffer)

    fun bindDefaultFrameBuffer()

    fun renderbufferStorage(internalFormat: GLInternalFormat, width: Int, height: Int)

    fun attachRenderBuffer(frameBuffer: GLFramebuffer, attachType: GLFramebufferAttachType, renderBuffer: GLRenderbuffer)

    fun attachTexture2d(textureId: Int, attachType: GLFramebufferAttachType)

    fun deleteRenderBuffer(id: Int)

    fun deleteFrameBuffer(id: Int)

    fun frameBufferTexture(attachType: GLFramebufferAttachType, texture: GLTexture)

    fun checkFrameBufferStatus(): Int

    fun getError(): Int
}
