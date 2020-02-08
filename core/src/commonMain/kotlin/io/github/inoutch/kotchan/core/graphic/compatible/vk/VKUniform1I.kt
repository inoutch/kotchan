package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1I

class VKUniform1I(
        buffer: VKValuePerSwapchainImage<VKUniformBuffer>,
        binding: Int,
        uniformName: String
) : VKUniform(buffer, binding, uniformName), Uniform1I {
    override val size: Int = Uniform1I.SIZE

    override fun set(value: Int) {
        buffer.value.copyToBuffer(intArrayOf(value), 0)
    }
}
