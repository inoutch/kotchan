#version 450 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texcoord;

layout(binding = 0) uniform Matrix {
    mat4 viewProjectionMatrix;
};

layout(location = 0) out vec4 outColor;
layout(location = 1) out vec2 outTexcoord;

vec3 positions[6] = vec3[](
vec3(0.0, -0.5, 0.0),
vec3(0.5, 0.5, 0.0),
vec3(-0.5, 0.5, 0.0),
vec3(0.0, -0.5, 0.0),
vec3(0.5, 0.5, 0.0),
vec3(-0.5, 0.5, 0.0)
);

void main(void) {
    outColor = color;
    outTexcoord = texcoord;
    gl_Position = viewProjectionMatrix * vec4(position, 1.0);
    gl_Position = vec4(positions[gl_VertexIndex], 1.0);
}
