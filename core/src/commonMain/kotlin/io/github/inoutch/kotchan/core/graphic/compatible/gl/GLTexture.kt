package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.TextureAddressMode
import io.github.inoutch.kotchan.core.graphic.compatible.TextureFilter
import io.github.inoutch.kotchan.core.graphic.compatible.TextureMipmapMode
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.gl.api.GL_LINEAR
import io.github.inoutch.kotlin.gl.api.GL_LINEAR_MIPMAP_LINEAR
import io.github.inoutch.kotlin.gl.api.GL_LINEAR_MIPMAP_NEAREST
import io.github.inoutch.kotlin.gl.api.GL_NEAREST
import io.github.inoutch.kotlin.gl.api.GL_NEAREST_MIPMAP_LINEAR
import io.github.inoutch.kotlin.gl.api.GL_NEAREST_MIPMAP_NEAREST
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_2D
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_MAG_FILTER
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_MIN_FILTER
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_WRAP_S
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_WRAP_T
import io.github.inoutch.kotlin.gl.api.GLint
import io.github.inoutch.kotlin.gl.api.gl

class GLTexture(override val size: Vector2I) : Texture() {
    private val id = gl.genTextures(1).first()

    private var isDisposedValue = false

    override var addressModeU: TextureAddressMode
        get() = super.addressModeU
        set(value) {
            bind()
            gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, value.glParam)
            super.addressModeU = value
        }

    override var addressModeV: TextureAddressMode
        get() = super.addressModeV
        set(value) {
            bind()
            gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, value.glParam)
            super.addressModeV = value
        }

    override var magFilter: TextureFilter
        get() = super.magFilter
        set(value) {
            bind()
            gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, computedMagFilter(value))
            super.magFilter = value
        }

    override var minFilter: TextureFilter
        get() = super.minFilter
        set(value) {
            bind()
            gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, computedMinFilter(minFilter, mipmapMode))
            super.minFilter = value
        }

    override var mipmapMode: TextureMipmapMode
        get() = super.mipmapMode
        set(value) {
            bind()
            gl.texParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, computedMinFilter(minFilter, mipmapMode))
            super.mipmapMode = value
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
        return when(magFilter) {
            TextureFilter.NEAREST -> GL_NEAREST
            TextureFilter.LINEAR -> GL_LINEAR
        }
    }

    private fun computedMinFilter(minFilter: TextureFilter, mipmapMode: TextureMipmapMode): GLint {
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
}
