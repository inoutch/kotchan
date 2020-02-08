package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1F

class VKUniform1F(
        buffer: VKValuePerSwapchainImage<VKUniformBuffer>,
        binding: Int,
        uniformName: String
) : VKUniform(buffer, binding, uniformName), Uniform1F {

    override val size: Int = Uniform1F.SIZE

    override fun set(value: Float) {
        buffer.value.copyToBuffer(floatArrayOf(value), 0)
    }
}
