#version 450

layout(binding = 1) uniform sampler2D texSampler;

layout(location=0) in vec4 color;
layout(location=1) in vec2 texcoord;

layout(location=0) out vec4 outColor;

void main(void) {
    vec4 c = texture(texSampler, texcoord) * color;
    if (c.a > 0.01) {
        outColor = c;
    } else {
        discard;
    }
}
