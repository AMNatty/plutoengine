#version 330 core

in vec2 position;
in vec2 uvCoords;
in int page;
in vec2 paintUVCoords;

uniform int paintType;

flat out float paintGradientAngleCos;
flat out float paintGradientAngleSin;
flat out float[2] paintGradientEndsUnrotated;
uniform vec2[2] paintGradientEnds;

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

	if (paintType == 1)
	{
		float dy = paintGradientEnds[1].y - paintGradientEnds[0].y;
		float dx = paintGradientEnds[1].x - paintGradientEnds[0].x;
		float paintGradientAngle = atan(-dy, dx);
		paintGradientAngleCos = cos(paintGradientAngle);
		paintGradientAngleSin = sin(paintGradientAngle);

		paintGradientEndsUnrotated[0] = paintGradientEnds[0].x * paintGradientAngleCos - paintGradientEnds[0].y * paintGradientAngleSin;
		paintGradientEndsUnrotated[1] = paintGradientEnds[1].x * paintGradientAngleCos - paintGradientEnds[1].y * paintGradientAngleSin;
	}

	gl_Position = projection * vec4(transformed, 1.0);
}