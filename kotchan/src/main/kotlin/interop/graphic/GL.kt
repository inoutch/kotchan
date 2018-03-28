package interop.graphic

import kotchan.view.Texture

expect class GL() {
    fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)
    fun viewPort(x: Int, y: Int, width: Int, height: Int)
    fun drawTriangleArrays(first: Int, count: Int)

    fun createVBO(size: Int): GLVBO
    fun createVBO(data: List<Float>): GLVBO
    fun updateVBO(vbo: GLVBO, offset: Int, data: List<Float>)
    fun vertexPointer(location: GLAttribLocation, interval: Int, vbo: GLVBO)
    fun compileShaderProgram(vertexShaderText: String, fragmentShaderText: String): GLShaderProgram
    fun useProgram(shaderProgram: GLShaderProgram)
    fun useTexture(texture: Texture?, index: Int)
    fun bindAttributeLocation(shaderProgram: GLShaderProgram, attributeLocation: GLAttribLocation, name: String)
    fun getUniform(shaderProgram: GLShaderProgram, name: String): Int
}