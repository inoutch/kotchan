#version 450 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texcoord;
layout(location = 3) in float texNumber;

layout(binding = 0) uniform Matrix {
    mat4 viewProjectionMatrix;
};

layout(location = 0) out vec4 outColor;
layout(location = 1) out vec2 outTexcoord;
layout(location = 2) out float outTexNumber;

void main(void) {
    outColor = color;
    outTexcoord = texcoord;
    outTexNumber = texNumber;
    gl_Position = viewProjectionMatrix * vec4(position, 1.0);
}
