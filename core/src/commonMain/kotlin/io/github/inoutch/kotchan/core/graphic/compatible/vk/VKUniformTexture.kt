package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTexture

class VKUniformTexture(
    binding: Int,
    uniformName: String
) : UniformTexture(binding, uniformName) {
    private var provider: VKDescriptorSetTextureProvider? = null

    override fun set(texture: Texture) {
        val provider = this.provider ?: throw IllegalStateException("Must bind provider before setting texture")
        provider.set(binding, texture as VKTexture)
    }

    fun bind(provider: VKDescriptorSetTextureProvider) {
        this.provider = provider
    }
}
