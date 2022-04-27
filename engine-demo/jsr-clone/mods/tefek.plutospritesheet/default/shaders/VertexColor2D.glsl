#version 330 core

in vec2 position;

uniform mat4 projection;
uniform mat3x2 transformation;

void main(void)
{
    gl_Position = projection * vec4(transformation * vec3(position.x, position.y, 1.0), 0.0, 1.0);
}