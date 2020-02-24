package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTextureArray

class VKUniformTextureArray(
    binding: Int,
    uniformName: String,
    val size: Int
) : UniformTextureArray(binding, uniformName) {
    private var provider: VKDescriptorSetTextureArrayProvider? = null

    override fun set(textures: List<Texture>) {
        val provider = this.provider ?: throw IllegalStateException("Must bind provider before setting texture")
        provider.set(binding, textures.map { it as VKTexture })
    }

    fun bind(provider: VKDescriptorSetTextureArrayProvider) {
        this.provider = provider
    }
}
