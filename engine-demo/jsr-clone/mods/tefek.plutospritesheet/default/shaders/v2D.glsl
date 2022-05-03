#version 330 core

in vec2 position;
in vec2 uvCoords;

out vec2 uvCoordinates;

uniform mat4 projection;
uniform mat3x2 transformation;

uniform vec2 uvBase;
uniform vec2 uvDelta;

void main(void)
{
	gl_Position = projection * vec4(transformation * vec3(position.x, position.y, 1.0), 0.0, 1.0);

	uvCoordinates = uvBase + uvCoords * uvDelta;
}