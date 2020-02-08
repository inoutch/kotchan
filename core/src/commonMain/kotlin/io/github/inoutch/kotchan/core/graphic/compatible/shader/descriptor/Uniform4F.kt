package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.core.Disposable
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotlin.gl.constant.FLOAT_BYTE_SIZE

interface Uniform4F : Disposable {
    companion object {
        const val SIZE = FLOAT_BYTE_SIZE * 4
    }

    fun set(value: Vector4F)
}
