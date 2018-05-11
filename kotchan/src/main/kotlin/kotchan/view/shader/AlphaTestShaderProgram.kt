package kotchan.view.shader

import utility.type.Matrix4


private const val AlphaTestVSource = """
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

private const val AlphaTestFSource = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

in vec4 vColor;
in vec2 vTexcoord;

uniform sampler2D u_texture0;
uniform float u_timeDelta;
uniform float u_textureEnable;
uniform float u_alpha;

out vec4 outColor;

void main(void)
{
    vec4 color = vColor;
    if (u_textureEnable >= 1.0) {
        color = color * texture(u_texture0, vTexcoord);
    }
    if (color.a >= u_alpha) {
        outColor = color;
    } else {
        discard;
    }
}
"""

open class AlphaTestShaderProgram : SimpleShaderProgram(AlphaTestVSource, AlphaTestFSource) {
    private val alphaLocation = gl.getUniform(this, "u_alpha")
    var alpha = 0.01f

    override fun prepare(delta: Float, mvpMatrix: Matrix4) {
        gl.uniform1f(alphaLocation, alpha)
        super.prepare(delta, mvpMatrix)
    }
}