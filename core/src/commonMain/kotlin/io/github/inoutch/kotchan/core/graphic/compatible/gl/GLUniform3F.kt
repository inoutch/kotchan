package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform3F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotlin.gl.api.GLuint
import io.github.inoutch.kotlin.gl.api.gl

class GLUniform3F(binding: Int, uniformName: String) : GLUniform(binding, uniformName), Uniform3F {
    override val size: Int = Uniform3F.SIZE

    private var value = Vector3F.Zero

    override fun set(value: Vector3F) {
        this.value = value
    }

    override fun copy(location: GLuint) {
        gl.uniform3f(location, value.x, value.y, value.z)
    }
}
