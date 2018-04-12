package kotchan.scene.shader

import interop.graphic.GLCamera
import interop.graphic.GLShaderProgram
import kotchan.Engine
import utility.type.Vector4

private const val NoColorsVSource = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

in vec4 point;
in vec2 texcoord;

uniform mat4 u_viewProjectionMatrix;

out vec2 vTexcoord;

void main(void){
    vTexcoord = texcoord;
    gl_Position = u_viewProjectionMatrix * point;
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

out vec4 outColor;

void main(void)
{
    outColor = u_color * texture(u_texture0, vTexcoord);
}
"""

class NoColorsShaderProgram : GLShaderProgram(Engine.getInstance().gl.compileShaderProgram(NoColorsVSource, NoColorsFSource)) {
    private val texture0Location = gl.getUniform(this, "u_texture0")
    private val colorLocation = gl.getUniform(this, "u_color")
    var color: Vector4 = Vector4(1.0f, 1.0f, 1.0f, 1.0f)

    override fun prepare(delta: Float, camera: GLCamera) {
        gl.uniform1i(texture0Location, 0)
        gl.uniform4f(colorLocation, color)
        super.prepare(delta, camera)
    }
}