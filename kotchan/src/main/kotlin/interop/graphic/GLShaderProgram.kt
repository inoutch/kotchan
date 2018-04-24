package interop.graphic

import kotchan.Engine

abstract class GLShaderProgram(val id: Int) {
    protected val gl: GL = Engine.getInstance().gl

    protected val viewProjectionMatrixLocation = gl.getUniform(id, "u_viewProjectionMatrix")
    protected val timeDeltaLocation = gl.getUniform(id, "u_timeDelta")
    private var isDestroy = false

    open fun prepare(delta: Float, camera: GLCamera) {
        gl.uniform1f(timeDeltaLocation, delta)
        gl.uniformMatrix4fv(viewProjectionMatrixLocation, 1, false, camera.combine)
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