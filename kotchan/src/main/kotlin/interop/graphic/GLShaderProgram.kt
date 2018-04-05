package interop.graphic

import kotchan.Engine

abstract class GLShaderProgram(val id: Int) {
    protected val gl: GL = Engine.getInstance().gl

    private val viewProjectionMatrixLocation = gl.getUniform(id, "u_viewProjectionMatrix")
    private val timeDeltaLocation = gl.getUniform(id, "u_timeDelta")
    private var isDeleted = false

    open fun prepare(delta: Float, camera: GLCamera) {
        gl.uniform1f(timeDeltaLocation, delta)
        gl.uniformMatrix4fv(viewProjectionMatrixLocation, 1, false, camera.combine)
    }

    fun use() {
        if (isDeleted) {
            throw Error("this shader is already deleted.")
        }
        gl.useProgram(this)
    }

    fun delete() {
        isDeleted = true
        gl.deleteShaderProgram(this)
    }
}