package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.core.graphic.shader.Descriptor
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKUniformBuffer

abstract class Uniform(binding: Int, uniformName: String) : Descriptor(binding, uniformName) {
    var vkUniform: VKUniformBuffer? = null
    var glUniform: Int? = null

    abstract val size: Long

    override fun dispose() {
        vkUniform?.dispose()
    }
}
