package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.CullMode
import io.github.inoutch.kotlin.gl.api.GL_BACK
import io.github.inoutch.kotlin.gl.api.GL_FRONT
import io.github.inoutch.kotlin.gl.api.GL_FRONT_AND_BACK
import io.github.inoutch.kotlin.gl.api.GL_NONE
import io.github.inoutch.kotlin.gl.api.GLint

private val cullModeMap = mapOf(
        CullMode.None to GL_NONE,
        CullMode.Front to GL_FRONT,
        CullMode.Back to GL_BACK,
        CullMode.FrontAndBack to GL_FRONT_AND_BACK
)

fun CullMode.toGLCullMode(): GLint {
    return cullModeMap.getValue(this)
}
