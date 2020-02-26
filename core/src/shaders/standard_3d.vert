#version 450 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texcoord;
layout(location = 3) in vec3 normal;

layout(binding = 0) uniform Matrix1 {
    mat4 viewProjectionMatrix;
};
layout(binding = 1) uniform Matrix2 {
    mat4 invViewProjectionMatrix;
};
layout(binding = 3) uniform Position {
    vec3 lightPosition;
};

layout(location = 0) out vec4 outColor;
layout(location = 1) out vec2 outTexcoord;
layout(location = 2) out vec3 outNormal;
layout(location = 3) out vec3 outLightPosition;
layout(location = 4) out vec3 outFragPosition;

void main(void) {
    outColor = color;
    outTexcoord = texcoord;
    outNormal = normal;
    outLightPosition = (invViewProjectionMatrix * vec4(outLightPosition, 1.0)).xyz;
    gl_Position = viewProjectionMatrix * vec4(position, 1.0);
    outFragPosition = gl_Position.xyz;
}
