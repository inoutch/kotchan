package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform4F
import io.github.inoutch.kotchan.math.Vector4F

class VKUniform4F(
    buffer: VKValuePerSwapchainImage<VKUniformBuffer>,
    binding: Int,
    uniformName: String
) : VKUniform(buffer, binding, uniformName), Uniform4F {
    override val size: Int = Uniform4F.SIZE

    override fun set(value: Vector4F) {
        buffer.value.copyToBuffer(floatArrayOf(value.x, value.y, value.z, value.w), 0)
    }
}
