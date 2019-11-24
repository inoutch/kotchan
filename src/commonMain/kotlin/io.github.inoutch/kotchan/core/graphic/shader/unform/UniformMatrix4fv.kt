package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.graphic.vulkan.FLOAT_SIZE
import io.github.inoutch.kotchan.utility.type.Matrix4

class UniformMatrix4fv(binding: Int, uniformName: String) : Uniform(binding, uniformName) {
    override val size = FLOAT_SIZE * 16

    fun set(value: Matrix4) {
        core.graphicsApi.setUniformMatrix4(this, value)
    }
}
