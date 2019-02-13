package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.graphic.GLShaderProgram
import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Vector4

private const val NoColorsVSource = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

in vec4 point;
in vec2 texcoord;

uniform mat4 u_mvpMatrix;

out vec2 vTexcoord;

void main(void){
    vTexcoord = texcoord;
    gl_Position = u_mvpMatrix * point;
}
"""

private const val NoColorsFSource = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

in vec2 vTexcoord;

uniform sampler2D u_texture0;
uniform float u_timeDelta;
uniform vec4 u_color;
uniform float u_textureEnable;

out vec4 outColor;

void main(void)
{
    if (u_textureEnable >= 1.0) {
        outColor = u_color * texture(u_texture0, vTexcoord);
    } else {
        outColor = u_color;
    }
}
"""

open class NoColorsShaderProgram protected constructor(vsh: String, fsh: String)
    : GLShaderProgram(KotchanCore.instance.gl.compileShaderProgram(vsh, fsh)) {

    constructor() : this(NoColorsVSource, NoColorsFSource)

    var color = Vector4(1.0f, 1.0f, 1.0f, 1.0f)

    var modelMatrix4 = Matrix4()

    private val texture0Location = gl.getUniform(this, "u_texture0")

    private val colorLocation = gl.getUniform(this, "u_color")

    private val mvpMatrixLocation = gl.getUniform(id, "u_mvpMatrix")

    override fun prepare(delta: Float, mvpMatrix: Matrix4) {
        gl.uniform1f(textureEnableLocation, if (textureEnable) 2.0f else 0.0f)
        gl.uniform1i(texture0Location, 0)
        gl.uniform4f(colorLocation, color)
        gl.uniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix * modelMatrix4)
        gl.uniform1f(timeDeltaLocation, delta)
    }
}
