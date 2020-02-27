#version 450

layout(binding = 2) uniform sampler2D texSampler;
layout(binding = 4) uniform Color {
    vec3 lightColor;
};
layout(binding = 5) uniform Ambient {
    float ambientStrength;
};

layout(location = 0) in vec4 color;
layout(location = 1) in vec2 texcoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 lightPosition;
layout(location = 4) in vec3 fragPosition;

layout(location = 0) out vec4 outColor;

void main(void) {
    vec3 ambient = ambientStrength * lightColor;

    vec3 lightDir = normalize(lightPosition - fragPosition);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    vec3 lightEffect = ambient + diffuse;
    outColor = texture(texSampler, texcoord) * color * vec4(lightEffect, 1.0);
}
