package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.TextureFilter
import io.github.inoutch.kotchan.core.graphic.compatible.TextureMipmapMode
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.gl.api.GL_LINEAR
import io.github.inoutch.kotlin.gl.api.GL_LINEAR_MIPMAP_LINEAR
import io.github.inoutch.kotlin.gl.api.GL_LINEAR_MIPMAP_NEAREST
import io.github.inoutch.kotlin.gl.api.GL_NEAREST
import io.github.inoutch.kotlin.gl.api.GL_NEAREST_MIPMAP_LINEAR
import io.github.inoutch.kotlin.gl.api.GL_NEAREST_MIPMAP_NEAREST
import io.github.inoutch.kotlin.gl.api.GL_RGBA
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_2D
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_MAG_FILTER
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_MIN_FILTER
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_WRAP_S
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_WRAP_T
import io.github.inoutch.kotlin.gl.api.GL_UNPACK_ALIGNMENT
import io.github.inoutch.kotlin.gl.api.GL_UNSIGNED_BYTE
import io.github.inoutch.kotlin.gl.api.GLint
import io.github.inoutch.kotlin.gl.api.gl

class GLTexture(image: Image, config: Config) : Texture(config) {
    override val size: Vector2I = image.size

    val id = gl.genTextures(1).first()

    private var isDisposedValue = false

    init {
        val format = GL_RGBA
        gl.bindTexture(GL_TEXTURE_2D, id)
        gl.pixelStorei(GL_UNPACK_ALIGNMENT, 1)
        gl.texImage2D(GL_TEXTURE_2D, 0, format, image.size.x, image.size.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.byteArray)

        gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, computedMinFilter(config.minFilter, config.mipmapMode))
        gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, computedMagFilter(config.magFilter))
        gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, config.addressModeU.glParam)
        gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, config.addressModeV.glParam)
        gl.bindTexture(GL_TEXTURE_2D, 0)
    }

    fun bind() {
        gl.bindTexture(GL_TEXTURE_2D, id)
    }

    override fun isDisposed(): Boolean {
        return isDisposedValue
    }

    override fun dispose() {
        if (isDisposedValue) {
            return
        }
        gl.deleteTextures(intArrayOf(id))
    }

    private fun computedMagFilter(magFilter: TextureFilter): GLint {
        // TODO: Improve performance using map
        return when (magFilter) {
            TextureFilter.NEAREST -> GL_NEAREST
            TextureFilter.LINEAR -> GL_LINEAR
        }
    }

    private fun computedMinFilter(
        minFilter: TextureFilter,
        mipmapMode: TextureMipmapMode,
        mipmapLevel: Int = 0
    ): GLint {
        // TODO: Improve performance using map
        if (mipmapLevel > 0) {
            return if (minFilter == TextureFilter.LINEAR && mipmapMode == TextureMipmapMode.LINER) {
                GL_LINEAR_MIPMAP_LINEAR
            } else if (minFilter == TextureFilter.LINEAR && mipmapMode == TextureMipmapMode.NEAREST) {
                GL_LINEAR_MIPMAP_NEAREST
            } else if (minFilter == TextureFilter.NEAREST && mipmapMode == TextureMipmapMode.LINER) {
                GL_NEAREST_MIPMAP_LINEAR
            } else {
                GL_NEAREST_MIPMAP_NEAREST
            }
        }
        return if (minFilter == TextureFilter.LINEAR) {
            GL_LINEAR
        } else {
            GL_NEAREST
        }
    }
}
