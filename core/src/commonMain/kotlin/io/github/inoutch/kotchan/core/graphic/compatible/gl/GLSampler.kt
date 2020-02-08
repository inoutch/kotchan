package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Sampler
import io.github.inoutch.kotlin.gl.api.gl

class GLSampler private constructor(
        binding: Int,
        descriptorName: String,
        private val id: Int
) : Sampler(binding, descriptorName) {
    companion object {
    }

    override fun set(texture: Texture) {
        check(texture is GLTexture)
        texture.bind()

        gl.uniform1i(id, binding)
        gl.activeTexture(binding)
    }
}