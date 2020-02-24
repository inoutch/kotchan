package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTextureArray
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE0
import io.github.inoutch.kotlin.gl.api.gl

class GLUniformTextureArray(
    binding: Int,
    descriptorName: String,
    val size: Int
) : UniformTextureArray(binding, descriptorName) {
    private var provider: GLUniformArrayLocationProvider? = null

    override fun set(textures: List<Texture>) {
        val provider = this.provider ?: return
        var i = 0
        while (i < textures.size) {
            gl.activeTexture(GL_TEXTURE0 + i)
            (textures[i] as GLTexture).bind()
            gl.uniform1i(provider.locations[i], i)
            i++
        }
    }

    fun bind(provider: GLUniformArrayLocationProvider) {
        this.provider = provider
    }
}
