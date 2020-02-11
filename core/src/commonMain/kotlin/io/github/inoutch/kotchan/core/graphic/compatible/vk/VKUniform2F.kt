package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform2F
import io.github.inoutch.kotchan.math.Vector2F

class VKUniform2F(
    buffer: VKValuePerSwapchainImage<VKUniformBuffer>,
    binding: Int,
    uniformName: String
) : VKUniform(buffer, binding, uniformName), Uniform2F {
    override val size: Int = Uniform2F.SIZE

    override fun set(value: Vector2F) {
        buffer.value.copyToBuffer(floatArrayOf(value.x, value.y), 0)
    }
}
