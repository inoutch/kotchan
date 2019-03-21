package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.graphic.vulkan.FLOAT_SIZE
import io.github.inoutch.kotchan.utility.type.Vector3

class Uniform3f(binding: Int, uniformName: String) : Uniform(binding, uniformName) {
    override val size = FLOAT_SIZE * 3

    fun set(value: Vector3) {
        KotchanCore.instance.graphicsApi.setUniform3f(this, value)
    }
}
