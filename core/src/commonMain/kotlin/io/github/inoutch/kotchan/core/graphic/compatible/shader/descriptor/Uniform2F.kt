package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotlin.gl.constant.FLOAT_BYTE_SIZE

interface Uniform2F : DescriptorSet {
    companion object {
        const val SIZE = FLOAT_BYTE_SIZE * 2
    }

    fun set(value: Vector2F)
}
