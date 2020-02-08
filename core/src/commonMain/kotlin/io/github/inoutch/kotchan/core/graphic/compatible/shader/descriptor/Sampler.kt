package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.core.graphic.compatible.Texture

abstract class Sampler(
        binding: Int,
        descriptorName: String
) : DescriptorSet(binding, descriptorName) {
    abstract fun set(texture: Texture)
}
