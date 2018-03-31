package interop.graphic

import kotchan.Engine

class GLShaderProgram(val id: Int) {
    //private val timeDeltaUniform: Int
    private val gl: GL = Engine.getInstance().gl

    init {
        //timeDeltaUniform = gl.getUniform(this, "u_timeDelta")
    }

    fun use() {
        gl.useProgram(this)
    }
}