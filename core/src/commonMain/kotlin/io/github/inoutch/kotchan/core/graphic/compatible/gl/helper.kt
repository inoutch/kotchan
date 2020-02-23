package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.graphic.BlendFactor
import io.github.inoutch.kotchan.core.graphic.CullMode
import io.github.inoutch.kotlin.gl.api.GL_BACK
import io.github.inoutch.kotlin.gl.api.GL_DST_ALPHA
import io.github.inoutch.kotlin.gl.api.GL_DST_COLOR
import io.github.inoutch.kotlin.gl.api.GL_FRONT
import io.github.inoutch.kotlin.gl.api.GL_FRONT_AND_BACK
import io.github.inoutch.kotlin.gl.api.GL_NONE
import io.github.inoutch.kotlin.gl.api.GL_ONE
import io.github.inoutch.kotlin.gl.api.GL_ONE_MINUS_DST_ALPHA
import io.github.inoutch.kotlin.gl.api.GL_ONE_MINUS_DST_COLOR
import io.github.inoutch.kotlin.gl.api.GL_ONE_MINUS_SRC_ALPHA
import io.github.inoutch.kotlin.gl.api.GL_ONE_MINUS_SRC_COLOR
import io.github.inoutch.kotlin.gl.api.GL_SRC_ALPHA
import io.github.inoutch.kotlin.gl.api.GL_SRC_COLOR
import io.github.inoutch.kotlin.gl.api.GL_ZERO
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

private val blendFactorMap = mapOf(
        BlendFactor.One to GL_ONE,
        BlendFactor.Zero to GL_ZERO,
        BlendFactor.SrcColor to GL_SRC_COLOR,
        BlendFactor.OneMinusSrcColor to GL_ONE_MINUS_SRC_COLOR,
        BlendFactor.DstColor to GL_DST_COLOR,
        BlendFactor.OneMinusDstColor to GL_ONE_MINUS_DST_COLOR,
        BlendFactor.SrcAlpha to GL_SRC_ALPHA,
        BlendFactor.OneMinusSrcAlpha to GL_ONE_MINUS_SRC_ALPHA,
        BlendFactor.DstAlpha to GL_DST_ALPHA,
        BlendFactor.OneMinusDstAlpha to GL_ONE_MINUS_DST_ALPHA
)

fun BlendFactor.toGLBlendFactor(): GLint {
    return blendFactorMap.getValue(this)
}
