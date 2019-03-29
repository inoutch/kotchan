package io.github.inoutch.kotchan.utility.graphic.gl

import io.github.inoutch.kotchan.utility.Disposable

class GLShader(val gl: GL, val id: Int) : Disposable {
    override fun dispose() {
        gl.deleteShaderProgram(this)
    }
}
