package kotchan

import interop.graphic.GL
import interop.graphic.GLShaderProgram
import interop.security.Security

class ShaderManager(private val gl: GL) {
    private val shaderProgramMap = mutableMapOf<String, GLShaderProgram>()

    fun getShaderProgram(vshSource: String, fshSource: String): GLShaderProgram {
        val key = Security.hash(vshSource + fshSource)
        return shaderProgramMap[key] ?: return gl.compileShaderProgram(vshSource, fshSource)
    }

    fun clearAllCaches() {
        shaderProgramMap.forEach { it.value.delete() }
    }
}