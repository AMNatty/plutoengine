#version 330 core

in vec2 uvCoordinates;

uniform sampler2D textureSampler;

uniform vec4 recolor;

out vec4 out_Color;

void main(void)
{
    vec4 color = texture(textureSampler, uvCoordinates) * recolor;

    out_Color = color;
}