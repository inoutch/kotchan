package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.graphic.vulkan.FLOAT_SIZE
import io.github.inoutch.kotchan.utility.type.Vector4

class Uniform4f(binding: Int, uniformName: String) : Uniform(binding, uniformName) {
    override val size = FLOAT_SIZE * 4

    fun set(value: Vector4) {
       core.graphicsApi.setUniform4f(this, value)
    }
}
