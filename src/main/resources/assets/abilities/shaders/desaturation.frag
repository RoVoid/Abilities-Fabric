#version 330 core

uniform sampler2D sampler;

in vec2 v_texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(sampler, v_texCoord);
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    fragColor = vec4(vec3(gray), color.a);
}
