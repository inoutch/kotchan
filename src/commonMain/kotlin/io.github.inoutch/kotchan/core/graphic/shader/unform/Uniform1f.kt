package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.graphic.vulkan.FLOAT_SIZE

class Uniform1f(binding: Int, uniformName: String) : Uniform(binding, uniformName) {
    override val size = FLOAT_SIZE * 1

    fun set(value: Float) {
        core.graphicsApi.setUniform1f(this, value)
    }
}
