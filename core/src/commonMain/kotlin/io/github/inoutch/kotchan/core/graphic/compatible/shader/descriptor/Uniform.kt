package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

abstract class Uniform(
        binding: Int,
        uniformName: String
): DescriptorSet(binding, uniformName) {
    abstract val size: Int
}
