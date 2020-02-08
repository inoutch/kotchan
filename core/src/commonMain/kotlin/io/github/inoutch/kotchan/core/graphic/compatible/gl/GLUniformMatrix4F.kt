package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotlin.gl.api.GLuint
import io.github.inoutch.kotlin.gl.api.gl

class GLUniformMatrix4F(
        binding: Int,
        uniformName: String
): GLUniform(binding, uniformName), UniformMatrix4F {
    override val size: Int = UniformMatrix4F.SIZE

    private var matrix = Matrix4F()

    override fun set(matrix: Matrix4F) {
        this.matrix = matrix
    }

    override fun copy(location: GLuint) {
        gl.uniformMatrix4fv(location, false, matrix.flatten())
    }
}
