package interop.graphic

import kotchan.Engine

class GLShaderProgram(val id: Int) {
    //private val timeDeltaUniform: Int
    private val gl: GL = Engine.getInstance().gl

    init {
        gl.bindAttributeLocation(this, GLAttribLocation.ATTRIBUTE_POSITION, "position")
        gl.bindAttributeLocation(this, GLAttribLocation.ATTRIBUTE_TEXCOORD, "texcoord")
        gl.bindAttributeLocation(this, GLAttribLocation.ATTRIBUTE_COLOR, "color")

        //timeDeltaUniform = gl.getUniform(this, "u_timeDelta")
    }

    fun use() {
        gl.useProgram(this)
    }
}