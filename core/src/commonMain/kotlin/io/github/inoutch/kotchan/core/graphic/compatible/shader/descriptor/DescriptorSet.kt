package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.core.Disposable

interface DescriptorSet : Disposable {
    val binding: Int
    val descriptorName: String
}
