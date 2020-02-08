package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1F
import io.github.inoutch.kotlin.gl.api.GLuint
import io.github.inoutch.kotlin.gl.api.gl

class GLUniform1F(binding: Int, uniformName: String) : GLUniform(binding, uniformName), Uniform1F {
    override val size: Int = Uniform1F.SIZE

    private var value = 0.0f

    override fun set(value: Float) {
        this.value = value
    }

    override fun copy(location: GLuint) {
        gl.uniform1f(location, value)
    }
}
