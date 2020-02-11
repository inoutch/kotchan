package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform
import io.github.inoutch.kotlin.gl.api.GLuint

abstract class GLUniform(
        binding: Int,
        uniformName: String
) : Uniform(binding, uniformName) {
    protected var provider: GLUniformLocationProvider? = null

    fun bind(provider: GLUniformLocationProvider) {
        this.provider = provider
    }
}
