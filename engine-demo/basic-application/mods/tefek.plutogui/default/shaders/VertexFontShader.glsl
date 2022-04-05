#version 330 core

in vec2 position;
in vec2 uvCoords;

out vec2 uvCoordinates;

uniform mat4 projection;
uniform mat4 transformation;

uniform vec2 uvBase;
uniform vec2 uvDelta;

uniform int italic;

void main(void)
{
	vec2 pos = vec2(position.x, position.y);
	
	if(italic == 1)
	{
		pos = vec2(position.x - position.y / 4.0, position.y);
	}
			
	uvCoordinates = uvBase + uvCoords * uvDelta;
	gl_Position = projection * transformation * vec4(pos, 0.0, 1.0);
}