package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform3F
import io.github.inoutch.kotchan.math.Vector3F

class VKUniform3F(
        buffer: VKValuePerSwapchainImage<VKUniformBuffer>,
        binding: Int,
        uniformName: String
) : VKUniform(buffer, binding, uniformName), Uniform3F {
    override val size: Int = Uniform3F.SIZE

    override fun set(value: Vector3F) {
        buffer.value.copyToBuffer(floatArrayOf(value.x, value.y, value.z), 0)
    }
}
