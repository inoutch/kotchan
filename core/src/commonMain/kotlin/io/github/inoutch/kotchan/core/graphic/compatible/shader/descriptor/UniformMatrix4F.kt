package io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor

import io.github.inoutch.kotchan.core.Disposable
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotlin.gl.constant.FLOAT_BYTE_SIZE

interface UniformMatrix4F : Disposable {
    companion object {
        const val SIZE = FLOAT_BYTE_SIZE * 4 * 4
    }

    fun set(matrix: Matrix4F)
}
