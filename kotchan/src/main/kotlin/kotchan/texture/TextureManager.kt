package kotchan.texture

import interop.graphic.GL
import interop.graphic.GLTexture

class TextureManager(private val gl: GL) {
    private val textureMap = mutableMapOf<String, GLTexture>()

    fun get(filepath: String?): GLTexture {
        if (filepath == null) {
            return GLTexture.empty
        }
        return textureMap[filepath] ?: gl.loadTexture(filepath) ?: GLTexture.empty
    }

    fun clearAll() {
        textureMap.forEach { gl.destroyTexture(it.value) }
    }
}