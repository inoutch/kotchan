package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1F
import io.github.inoutch.kotlin.gl.api.gl

class GLUniform1F(binding: Int, uniformName: String) : GLUniform(binding, uniformName), Uniform1F {
    override val size: Int = Uniform1F.SIZE

    override fun set(value: Float) {
        val provider = this.provider ?: return
        gl.uniform1f(provider.location, value)
    }
}
