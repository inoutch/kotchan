package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Matrix4

abstract class ShaderProgram(val id: Int) : Disposable {

    class ShaderSource(
            val text: String,
            val binary: ByteArray)

    var textureEnable = true

    protected val viewProjectionMatrixLocation = gl.getUniform(id, "u_viewProjectionMatrix")

    protected val timeDeltaLocation = gl.getUniform(id, "u_timeDelta")

    protected val textureEnableLocation = gl.getUniform(id, "u_textureEnable")

    open fun prepare(delta: Float, mvpMatrix: Matrix4) {
        gl.uniform1f(timeDeltaLocation, delta)
        gl.uniform1f(textureEnableLocation, if (textureEnable) 2.0f else 0.0f)
        gl.uniformMatrix4fv(viewProjectionMatrixLocation, 1, false, mvpMatrix)
    }

    fun use() {
        gl.useProgram(this.id)
    }

    override fun dispose() {
        gl.deleteShaderProgram(this)
    }
}
