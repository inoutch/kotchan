package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTexture
import io.github.inoutch.kotlin.gl.api.gl

class GLUniformTexture(
    binding: Int,
    descriptorName: String
) : UniformTexture(binding, descriptorName) {
    private var provider: GLUniformLocationProvider? = null

    override fun set(texture: Texture) {
        val provider = this.provider ?: return

        check(texture is GLTexture)
        texture.bind()

        gl.uniform1i(provider.location, binding)
        gl.activeTexture(binding)
    }

    fun bind(provider: GLUniformLocationProvider) {
        this.provider = provider
    }
}
