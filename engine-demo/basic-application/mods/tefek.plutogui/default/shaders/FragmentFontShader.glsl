#version 330 core

in vec2 uvCoordinates;
flat in int atlasPage;
in vec2 paintUVCoordinates;

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

vec4 gradientColor(void)
{
    float par = smoothstep(paintGradientEnds[0].x, paintGradientEnds[1].x, paintUVCoordinates.x) * (paintGradientStopCount - 1);

    float nPar = clamp(par, 0, float(paintGradientStopCount - 1)) + paintGradientPositions[0] * 0.000001;
    vec4 lCol = paintGradientColors[int(floor(nPar))];
    vec4 rCol = paintGradientColors[int(ceil(nPar))];

    float gamma = 0.45;

    vec4 ilCol = vec4(pow(lCol.r, 1 / gamma), pow(lCol.g, 1 / gamma), pow(lCol.b, 1 / gamma), lCol.a);
    vec4 irCol = vec4(pow(rCol.r, 1 / gamma), pow(rCol.g, 1 / gamma), pow(rCol.b, 1 / gamma), rCol.a);

    vec4 fCol = mix(ilCol, irCol, fract(nPar));

    return vec4(pow(fCol.r, gamma), pow(fCol.g, gamma), pow(fCol.b, gamma), fCol.a);
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