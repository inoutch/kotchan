package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.destruction.StrictDestruction
import io.github.inoutch.kotchan.utility.type.Matrix4

abstract class GLShaderProgram(val id: Int) : StrictDestruction() {

    var textureEnable = true

    protected val gl: GL = KotchanCore.instance.gl

    protected val viewProjectionMatrixLocation = gl.getUniform(id, "u_viewProjectionMatrix")

    protected val timeDeltaLocation = gl.getUniform(id, "u_timeDelta")

    protected val textureEnableLocation = gl.getUniform(id, "u_textureEnable")

    open fun prepare(delta: Float, mvpMatrix: Matrix4) {
        gl.uniform1f(timeDeltaLocation, delta)
        gl.uniform1f(textureEnableLocation, if (textureEnable) 2.0f else 0.0f)
        gl.uniformMatrix4fv(viewProjectionMatrixLocation, 1, false, mvpMatrix)
    }

    fun use() {
        check()
        gl.useProgram(this.id)
    }

    override fun destroy() {
        super.destroy()
        gl.deleteShaderProgram(this)
    }
}