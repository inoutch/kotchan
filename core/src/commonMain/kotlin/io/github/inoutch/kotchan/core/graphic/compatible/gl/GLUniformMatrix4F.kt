package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotlin.gl.api.gl

class GLUniformMatrix4F(
    binding: Int,
    uniformName: String
) : GLUniform(binding, uniformName), UniformMatrix4F {
    override val size: Int = UniformMatrix4F.SIZE

    override fun set(matrix: Matrix4F) {
        val provider = this.provider ?: return
        gl.uniformMatrix4fv(provider.location, false, matrix.flatten())
    }
}
