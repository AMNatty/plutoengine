#version 330 core

in vec2 uvCoordinates;
in vec2 paintUVCoordinates;

uniform sampler2DRect textureSampler;

uniform int paintType;
uniform vec4 paintColor;

uniform int paintGradientStopCount;
uniform vec4[16] paintGradientColors;
uniform float[16] paintGradientPositions;
uniform vec2[2] paintGradientEnds;

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
    float angle = atan(-paintGradientEnds[1].y + paintGradientEnds[0].y, paintGradientEnds[1].x - paintGradientEnds[0].x);
    float rotatedStartX = paintGradientEnds[0].x * cos(angle) - paintGradientEnds[0].y * sin(angle);
    float rotatedEndX = paintGradientEnds[1].x * cos(angle) - paintGradientEnds[1].y * sin(angle);
    float d = rotatedEndX - rotatedStartX;

    float pX = paintUVCoordinates.x * cos(angle) - paintUVCoordinates.y * sin(angle);

    float mr = smoothstep(rotatedStartX + paintGradientPositions[0] * d, rotatedStartX + paintGradientPositions[1] * d, pX);
    vec4 col = gammaMix(paintGradientColors[0], paintGradientColors[1], mr);

    for (int i = 1; i < paintGradientStopCount - 1; i++)
    {
        mr = smoothstep(rotatedStartX + paintGradientPositions[i] * d, rotatedStartX + paintGradientPositions[i + 1] * d, pX);
        col = gammaMix(col, paintGradientColors[i + 1], mr);
    }

    return col;
}

void main(void)
{
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

    col.rgba *= texture(textureSampler, uvCoordinates);

    out_Color = col;
}