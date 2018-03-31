package interop.graphic

val SimpleVertexShaderSource = """
#if __VERSION__ >= 130
    #define attribute in
    #define varying out
#endif

#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

attribute vec4 position;
attribute vec4 color;
attribute vec2 texcoord;

varying vec4 vColor;
varying vec2 vTexcoord;

void main(void){
    vColor = color;
    vTexcoord = texcoord;
    gl_Position = position;
}
"""

val SimpleFragmentShaderSource = """
#if __VERSION__ >= 130
    #define varying in
    out vec4 mgl_FragColor;
    #define texture2D texture
    #define gl_FragColor mgl_FragColor
#endif

#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

varying vec4 vColor;
varying vec2 vTexcoord;

void main(void)
{
    gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);
}
"""