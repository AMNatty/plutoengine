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

in vec2 uvCoordinates;
flat in int atlasPage;
in vec2 paintUVCoordinates;

flat in float paintGradientAngleCos;
flat in float paintGradientAngleSin;
flat in float[2] paintGradientEndsUnrotated;

uniform sampler2DArray textureSampler;

uniform int paintType;
uniform vec4 paintColor;

uniform int paintGradientStopCount;
uniform vec4[16] paintGradientColors;
uniform float[16] paintGradientPositions;
uniform vec2[2] paintGradientEnds;

uniform float pxScale;

out vec4 out_Color;

vec4 solidColor(void)
{
    return paintColor;
}

vec4 gammaMix(vec4 lCol, vec4 rCol, float ratio)
{
    float gamma = 2.2;
    float one_over_gamma = 1 / gamma;

    vec4 ilCol = vec4(pow(lCol.r, gamma), pow(lCol.g, gamma), pow(lCol.b, gamma), lCol.a);
    vec4 irCol = vec4(pow(rCol.r, gamma), pow(rCol.g, gamma), pow(rCol.b, gamma), rCol.a);

    vec4 fCol = mix(ilCol, irCol, ratio);

    return vec4(pow(fCol.r, one_over_gamma), pow(fCol.g, one_over_gamma), pow(fCol.b, one_over_gamma), fCol.a);
}

vec4 gradientColor(void)
{
    float tStart = paintGradientEndsUnrotated[0];
    float gradientLength = paintGradientEndsUnrotated[1] - tStart;

    float t = paintUVCoordinates.x * paintGradientAngleCos - paintUVCoordinates.y * paintGradientAngleSin;

    float ti = paintGradientPositions[0] * gradientLength;
    float tni;
    vec4 col = paintGradientColors[0];

    for (int i = 0; i < paintGradientStopCount - 1; i++)
    {
        tni = paintGradientPositions[i + 1] * gradientLength;

        float mixValue = smoothstep(tStart + ti, tStart + tni, t);
        col = gammaMix(col, paintGradientColors[i + 1], mixValue);

        ti = tni;
    }

    return col;
}

void main(void)
{
    vec3 texCoords = vec3(uvCoordinates, atlasPage);

    float threshold = 180.0 / 255.0 - 5.0 / pow(pxScale, 1.6); // Also help small text be readable

    float signedDist = texture(textureSampler, texCoords).r - threshold;

    vec4 col;

    switch (paintType)
    {
        case 0:
            col = solidColor();
            break;

        case 1:
            col = gradientColor();
            break;
    }

    col.a *= smoothstep(0, 2.4 / pxScale, signedDist);

    out_Color = col;
}