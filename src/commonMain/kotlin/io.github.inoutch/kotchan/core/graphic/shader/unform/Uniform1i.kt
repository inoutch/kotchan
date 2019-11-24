package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.utility.graphic.vulkan.INT_SIZE

class Uniform1i(binding: Int, uniformName: String) : Uniform(binding, uniformName) {
    override val size = INT_SIZE * 1

    fun set(value: Int) {
        core.graphicsApi.setUniform1i(this, value)
    }
}
