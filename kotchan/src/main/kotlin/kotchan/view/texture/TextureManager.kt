package kotchan.view.texture

import interop.graphic.GL
import interop.graphic.GLTexture
import kotchan.Engine
import kotchan.logger.logger
import kotchan.view.drawable.Square
import utility.type.Vector2

class TextureManager(private val gl: GL) {
    private val textureMap = mutableMapOf<String, GLTexture>()

    fun get(filepath: String?): GLTexture {
        if (filepath == null) {
            logger.error("texture is not found[$filepath]")
            return GLTexture.empty
        }
        return textureMap.getOrPut(filepath, {
            val texture = gl.loadTexture(filepath)
            if (texture == null) {
                logger.error("texture is not loaded[$filepath]")
                GLTexture.empty
            } else {
                texture
            }
        })
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
        textureMap.forEach { it.value.destroy() }
        textureMap.clear()
    }
}