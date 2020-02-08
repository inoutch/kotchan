package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform2F
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotlin.gl.api.GLuint
import io.github.inoutch.kotlin.gl.api.gl

class GLUniform2F(
        binding: Int,
        uniformName: String
) : GLUniform(binding, uniformName), Uniform2F {
    override val size: Int = Uniform2F.SIZE

    private var value = Vector2F.Zero

    override fun set(value: Vector2F) {
        this.value = value
    }

    override fun copy(location: GLuint) {
        gl.uniform2f(location, value.x, value.y)
    }
}