package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.core.Disposer

abstract class Uniform(
    override val binding: Int,
    override val descriptorName: String
) : Disposer(), DescriptorSet {
    abstract val size: Int
}
