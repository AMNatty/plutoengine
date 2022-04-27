#version 330 core

in vec2 position;
in vec2 uvCoords;
in int page;
in vec2 paintUVCoords;

out vec2 uvCoordinates;
out vec2 paintUVCoordinates;
flat out int atlasPage;

uniform mat4 projection;
uniform mat3 transformation;

void main(void)
{
	atlasPage = page;
	uvCoordinates = uvCoords;
	paintUVCoordinates = paintUVCoords;
	vec3 transformed = vec3((transformation * vec3(position, 1.0)).xy, 0.0);
	gl_Position = projection * vec4(transformed, 1.0);
}