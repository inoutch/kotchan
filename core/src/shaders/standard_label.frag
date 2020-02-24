#version 450

layout(binding = 1) uniform sampler2D texSamplers[8];

layout(location = 0) in vec4 color;
layout(location = 1) in vec2 texcoord;
layout(location = 2) in float texNumber;

layout(location = 0) out vec4 outColor;

void main(void) {
    outColor = texture(texSamplers[int(texNumber)], texcoord) * color;
}
