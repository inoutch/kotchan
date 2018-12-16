package io.github.inoutch.kotchan.core.view.texture

import io.github.inoutch.kotchan.utility.graphic.GL
import io.github.inoutch.kotchan.utility.graphic.GLTexture
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.logger.logger
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.core.view.drawable.Square

class TextureManager(private val gl: GL) {

    private val textureMap = mutableMapOf<String, GLTexture>()

    fun get(filepath: String?): GLTexture {
        if (filepath == null) {
            logger.error("texture is not found[$filepath]")
            return GLTexture.empty
        }
        return textureMap.getOrPut(filepath) {
            val texture = gl.loadTexture(filepath)
            if (texture == null) {
                logger.error("texture is not loaded[$filepath]")
                GLTexture.empty
            } else {
                texture
            }
        }
    }

    fun getFromResource(filepath: String): GLTexture {
        val fullPath = KotchanCore.instance.file.getResourcePath(filepath)
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