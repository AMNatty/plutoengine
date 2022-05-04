/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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