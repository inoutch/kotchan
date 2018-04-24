package kotchan.texture

import interop.graphic.GL
import interop.graphic.GLTexture
import kotchan.Engine
import kotchan.scene.drawable.Square
import utility.type.Vector2

class TextureManager(private val gl: GL) {
    private val textureMap = mutableMapOf<String, GLTexture>()

    fun get(filepath: String?): GLTexture {
        if (filepath == null) {
            return GLTexture.empty
        }
        return textureMap[filepath] ?: gl.loadTexture(filepath) ?: GLTexture.empty
    }

    fun getFromResource(filepath: String): GLTexture {
        val fullPath = Engine.getInstance().file.getResourcePath(filepath)
        return get(fullPath)
    }

    fun createSquare(filepath: String?): Square {
        val texture = get(filepath)
        return Square(Vector2(texture.width.toFloat(), texture.height.toFloat()), texture)
    }

    fun clearAll() {
        textureMap.clear()
    }
}