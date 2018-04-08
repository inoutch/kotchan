package kotchan.scene.shader

import interop.graphic.GLCamera
import interop.graphic.GLShaderProgram
import kotchan.Engine

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

out vec4 outColor;

void main(void)
{
    outColor = vColor * texture(u_texture0, vTexcoord);
}
"""

class SimpleShaderProgram : GLShaderProgram(Engine.getInstance().gl.compileShaderProgram(SimpleVSource, SimpleFSource)) {
    private val texture0Location = gl.getUniform(this, "u_texture0")

    override fun prepare(delta: Float, camera: GLCamera) {
        gl.uniform1i(texture0Location, 0)
        super.prepare(delta, camera)
    }
}