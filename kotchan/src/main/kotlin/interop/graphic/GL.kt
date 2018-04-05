package interop.graphic

import utility.type.Matrix4

expect class GL() {
    fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)

    fun viewPort(x: Int, y: Int, width: Int, height: Int)

    fun drawTriangleArrays(first: Int, count: Int)

    fun createVBO(size: Int): GLVBO

    fun createVBO(data: FloatArray): GLVBO

    fun updateVBO(vbo: GLVBO, offset: Int, data: FloatArray)

    fun vertexPointer(location: GLAttribLocation, interval: Int, vbo: GLVBO)

    fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): Int

    fun deleteShaderProgram(shaderProgram: GLShaderProgram)

    fun useProgram(shaderProgram: GLShaderProgram)

    fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String)

    // getter
    fun getUniform(shaderProgram: GLShaderProgram, name: String): Int

    fun getUniform(shaderProgramId: Int, name: String): Int

    fun getAttribLocation(shaderProgram: GLShaderProgram, name: String): Int

    // uniform
    fun uniform1i(location: Int, v0: Int)

    fun uniform1f(location: Int, v0: Float)

    fun uniformMatrix4fv(location: Int, count: Int, transpose: Boolean, matrix: Matrix4)

    // blend
    fun enableBlend()

    // texture
    fun enableTexture()

    fun disableTexture()

    fun activeTexture(index: Int)

    fun useTexture(texture: GLTexture?)

    fun loadTexture(filepath: String): GLTexture?

    fun destroyTexture(texture: GLTexture)

    fun filterTexture(type: GLFilterType)

    fun debug()
}