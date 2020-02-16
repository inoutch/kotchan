#version 450

layout(binding = 1) uniform sampler2D texSampler;

layout(location=0) in vec4 color;
layout(location=1) in vec2 texcoord;

layout(location=0) out vec4 outColor;

void main(void) {
    outColor = texture(texSampler, texcoord) * color;
    outColor = vec4(outColor.rgb, 1.0);
}
