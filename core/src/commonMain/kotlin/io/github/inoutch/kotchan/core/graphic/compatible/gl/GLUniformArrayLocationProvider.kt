package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotlin.gl.api.gl

class GLUniformArrayLocationProvider(
        shader: GLShader,
        uniformName: String,
        size: Int
) {
    val locations = List(size) { gl.getUniformLocation(shader.program, "$uniformName[$it]") }
}