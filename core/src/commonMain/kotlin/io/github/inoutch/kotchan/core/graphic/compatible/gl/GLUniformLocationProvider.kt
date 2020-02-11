package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotlin.gl.api.gl

class GLUniformLocationProvider(
        shader: GLShader,
        uniformName: String
) {
    val location = gl.getUniformLocation(shader.program, uniformName)
}