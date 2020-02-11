package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1I
import io.github.inoutch.kotlin.gl.api.GLuint
import io.github.inoutch.kotlin.gl.api.gl
import io.github.inoutch.kotlin.gl.constant.INT_BYTE_SIZE

class GLUniform1I(
        binding: Int,
        uniformName: String
) : Uniform1I, GLUniform(binding, uniformName) {
    override val size: Int = 1 * INT_BYTE_SIZE

    override fun set(value: Int) {
        val provider = this.provider ?: return
        gl.uniform1i(provider.location, value)
    }
}