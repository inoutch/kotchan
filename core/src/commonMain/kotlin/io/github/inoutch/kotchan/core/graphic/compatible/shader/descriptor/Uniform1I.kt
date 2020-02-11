package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotlin.gl.constant.INT_BYTE_SIZE

interface Uniform1I : DescriptorSet {
    companion object {
        const val SIZE = INT_BYTE_SIZE * 1
    }

    fun set(value: Int)
}
