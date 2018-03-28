package interop.graphic

val SimpleVertexShaderSource = """
#version 410
#ifdef GL_ES
precision mediump float;
#endif

in vec4 position;
in vec4 color;

out vec4 vColor;

void main(void){
    vColor = color;
    gl_Position = position;
}
"""

val SimpleFragmentShaderSource = """
#version 410
#ifdef GL_ES
precision mediump float;
#endif

in vec4 vColor;

out vec4 color;

void main(void)
{
    color = vColor;
}
"""