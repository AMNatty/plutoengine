#version 330 core

in vec2 uvCoordinates;
flat in int atlasPage;

uniform sampler2DArray textureSampler;
uniform vec4 recolor;

out vec4 out_Color;

void main(void)
{
    vec3 textCoords = vec3(uvCoordinates, atlasPage);

    float threshold = 1 - 240.0 / 255.0;

    float signedDist = 1 - texture(textureSampler, textCoords).r;

    float fw = fwidth(signedDist);

    vec4 col = recolor;

    col.a *= smoothstep(threshold + 0.2, threshold, signedDist);

    out_Color = col;
}