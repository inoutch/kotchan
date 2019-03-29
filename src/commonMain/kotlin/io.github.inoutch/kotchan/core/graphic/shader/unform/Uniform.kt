package io.github.inoutch.kotchan.core.graphic.shader.unform

import io.github.inoutch.kotchan.core.graphic.shader.DescriptorSet
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKUniformBuffer

abstract class Uniform(binding: Int, uniformName: String) : DescriptorSet(binding, uniformName) {
    var vkUniform: VKUniformBuffer? = null
    var glUniform: Int? = null

    abstract val size: Long

    override fun dispose() {
        // DO NOT dispose uniform in here
    }
}
