package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotlin.gl.constant.FLOAT_BYTE_SIZE

interface Uniform1F : DescriptorSet {
    companion object {
        const val SIZE = FLOAT_BYTE_SIZE * 1
    }

    fun set(value: Float)
}