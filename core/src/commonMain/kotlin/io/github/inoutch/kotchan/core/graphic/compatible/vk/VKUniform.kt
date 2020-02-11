package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform

abstract class VKUniform(
    val buffer: VKValuePerSwapchainImage<VKUniformBuffer>,
    binding: Int,
    uniformName: String
) : Uniform(binding, uniformName) {
    protected var provider: VKDescriptorSetUniformProvider? = null

    fun bind(provider: VKDescriptorSetUniformProvider) {
        this.provider = provider
    }
}
