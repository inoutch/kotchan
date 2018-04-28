package kotchan.scene.shader

import kotchan.camera.Camera
import interop.graphic.GLShaderProgram
import kotchan.Engine
import utility.type.Matrix4
import utility.type.Vector4

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

class NoColorsShaderProgram : GLShaderProgram(Engine.getInstance().gl.compileShaderProgram(NoColorsVSource, NoColorsFSource)) {
    private val texture0Location = gl.getUniform(this, "u_texture0")
    private val colorLocation = gl.getUniform(this, "u_color")
    private val mvpMatrixLocation = gl.getUniform(id, "u_mvpMatrix")
    var color: Vector4 = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
    var modelMatrix4 = Matrix4()

    override fun prepare(delta: Float, mvpMatrix: Matrix4) {
        gl.uniform1f(textureEnableLocation, if (textureEnable) 2.0f else 0.0f)
        gl.uniform1i(texture0Location, 0)
        gl.uniform4f(colorLocation, color)
        gl.uniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix * modelMatrix4)
        gl.uniform1f(timeDeltaLocation, delta)
    }
}