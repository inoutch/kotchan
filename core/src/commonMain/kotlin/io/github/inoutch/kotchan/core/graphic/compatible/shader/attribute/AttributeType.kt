package io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute

import io.github.inoutch.kotlin.gl.constant.FLOAT_BYTE_SIZE
import io.github.inoutch.kotlin.gl.constant.INT_BYTE_SIZE

enum class AttributeType(val size: Int) {
    INT(INT_BYTE_SIZE),
    FLOAT(FLOAT_BYTE_SIZE),
}