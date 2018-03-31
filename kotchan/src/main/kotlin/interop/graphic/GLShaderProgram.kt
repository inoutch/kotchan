package interop.graphic

import kotchan.Engine

class GLShaderProgram(val id: Int) {
    //private val timeDeltaUniform: Int
    private val gl: GL = Engine.getInstance().gl
    private var isDeteled = false

    init {
        //timeDeltaUniform = gl.getUniform(this, "u_timeDelta")
    }

    fun use() {
        if (isDeteled) {
            // TODO: throw exception
        }
        gl.useProgram(this)
    }

    fun delete() {
        isDeteled = true
        gl.deleteShaderProgram(this)
    }
}