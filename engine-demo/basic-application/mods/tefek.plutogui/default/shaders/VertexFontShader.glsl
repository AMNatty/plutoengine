#version 330 core

in vec2 position;
in vec2 uvCoords;
in int page;

out vec2 uvCoordinates;
flat out int atlasPage;

uniform mat4 projection;
uniform mat3 transformation;

uniform int italic;

void main(void)
{
	vec2 pos = vec2(position.x - position.y * italic / 4.0, position.y);
	atlasPage = page;
	uvCoordinates = uvCoords;
	vec3 transformed = vec3((transformation * vec3(pos, 1.0)).xy, 0.0);
	gl_Position = projection * vec4(transformed, 1.0);
}