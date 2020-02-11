package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.compatible.Texture

abstract class UniformTexture(
    override val binding: Int,
    override val descriptorName: String
) : Disposer(), DescriptorSet {
    abstract fun set(texture: Texture)
}
