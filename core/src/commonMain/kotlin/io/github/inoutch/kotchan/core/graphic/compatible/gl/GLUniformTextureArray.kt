package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTextureArray
import io.github.inoutch.kotlin.gl.api.gl

class GLUniformTextureArray(
        binding: Int,
        descriptorName: String
) : UniformTextureArray(binding, descriptorName) {
    private var provider: GLUniformLocationProvider? = null

    override fun set(textures: List<Texture>) {
        val provider = this.provider ?: return
        val ids = textures.map { (it as GLTexture).id }
        gl.uniform1iv(provider.location, ids.toIntArray())
    }
}
