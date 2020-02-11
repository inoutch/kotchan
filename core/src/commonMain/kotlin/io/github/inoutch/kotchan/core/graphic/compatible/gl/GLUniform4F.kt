package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform4F
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotlin.gl.api.gl

class GLUniform4F(
        binding: Int,
        uniformName: String
) : GLUniform(binding, uniformName), Uniform4F {
    override val size: Int = Uniform4F.SIZE

    override fun set(value: Vector4F) {
        val provider = this.provider ?: return
        gl.uniform4f(provider.location, value.x, value.y, value.z, value.w)
    }
}
