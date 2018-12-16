package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.type.Matrix4

abstract class GLShaderProgram(val id: Int) {

    var textureEnable = true

    protected val gl: GL = KotchanCore.instance.gl

    protected val viewProjectionMatrixLocation = gl.getUniform(id, "u_viewProjectionMatrix")

    protected val timeDeltaLocation = gl.getUniform(id, "u_timeDelta")

    protected val textureEnableLocation = gl.getUniform(id, "u_textureEnable")

    private var isDestroy = false

    open fun prepare(delta: Float, mvpMatrix: Matrix4) {
        gl.uniform1f(timeDeltaLocation, delta)
        gl.uniform1f(textureEnableLocation, if (textureEnable) 2.0f else 0.0f)
        gl.uniformMatrix4fv(viewProjectionMatrixLocation, 1, false, mvpMatrix)
    }

    fun use() {
        if (isDestroy) {
            throw Error("this shader is already deleted.")
        }
        gl.useProgram(this.id)
    }

    fun destroy() {
        isDestroy = true
        gl.deleteShaderProgram(this)
    }
}