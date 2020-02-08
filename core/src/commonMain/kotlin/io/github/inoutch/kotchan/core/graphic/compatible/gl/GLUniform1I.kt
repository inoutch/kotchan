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

    private var value: Int = 0

    override fun set(value: Int) {
        this.value = value
    }

    override fun copy(location: GLuint) {
        gl.uniform1i(location, value)
    }
}