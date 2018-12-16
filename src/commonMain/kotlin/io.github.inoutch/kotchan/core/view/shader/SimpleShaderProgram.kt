package io.github.inoutch.kotchan.core.view.shader

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.graphic.GLShaderProgram
import io.github.inoutch.kotchan.utility.type.Matrix4

private const val SimpleVSource = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

in vec4 point;
in vec4 color;
in vec2 texcoord;

uniform mat4 u_viewProjectionMatrix;

out vec4 vColor;
out vec2 vTexcoord;

void main(void){
    vColor = color;
    vTexcoord = texcoord;
    gl_Position = u_viewProjectionMatrix * point;
}
"""

private const val SimpleFSource = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

in vec4 vColor;
in vec2 vTexcoord;

uniform sampler2D u_texture0;
uniform float u_timeDelta;
uniform float u_textureEnable;

out vec4 outColor;

void main(void)
{
    outColor = vColor;
    if (u_textureEnable >= 1.0) {
        outColor = outColor * texture(u_texture0, vTexcoord);
    }
}
"""

open class SimpleShaderProgram(vsh: String = SimpleVSource, fsh: String = SimpleFSource) : GLShaderProgram(KotchanCore.instance.gl.compileShaderProgram(vsh, fsh)) {
    private val texture0Location = gl.getUniform(this, "u_texture0")

    override fun prepare(delta: Float, mvpMatrix: Matrix4) {
        gl.uniform1i(texture0Location, 0)
        super.prepare(delta, mvpMatrix)
    }
}