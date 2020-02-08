package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform4F
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotlin.gl.api.GLuint
import io.github.inoutch.kotlin.gl.api.gl

class GLUniform4F(
        binding: Int,
        uniformName: String
) : GLUniform(binding, uniformName), Uniform4F {
    override val size: Int = Uniform4F.SIZE

    private var value = Vector4F.Zero

    override fun set(value: Vector4F) {
        this.value = value
    }

    override fun copy(location: GLuint) {
        gl.uniform4f(location, value.x, value.y, value.z, value.w)
    }
}
