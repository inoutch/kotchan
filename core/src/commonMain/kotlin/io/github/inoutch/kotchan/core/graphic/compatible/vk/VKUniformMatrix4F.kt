package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.math.Matrix4F

class VKUniformMatrix4F(
        buffer: VKValuePerSwapchainImage<VKUniformBuffer>,
        binding: Int,
        uniformName: String
) : VKUniform(buffer, binding, uniformName), UniformMatrix4F {
    override val size: Int = UniformMatrix4F.SIZE

    override fun set(matrix: Matrix4F) {
        buffer.value.copyToBuffer(matrix.flatten(), 0)
    }
}
