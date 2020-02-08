package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.core.Disposer

abstract class DescriptorSet(
        val binding: Int,
        val descriptorName: String
) : Disposer()
