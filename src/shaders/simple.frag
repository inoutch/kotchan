#version 450

//layout(binding = 1) uniform sampler2D texSampler;

layout(location=0) in vec4 color;
layout(location=1) in vec2 texcoord;

layout(location=0) out vec4 outColor;

void main(void) {
//    outColor = texture(texSampler, texcoord) * vec4(color, 1.0);
    outColor = vec4(texcoord.x, texcoord.y, 1.0, 1.0) * color;
}
