package org.plutoengine.graphics.font;

import org.plutoengine.graphics.gl.vao.attrib.ReservedAttributes;
import org.plutoengine.shader.ShaderBase;
import org.plutoengine.shader.ShaderProgram;
import org.plutoengine.shader.VertexArrayAttribute;
import org.plutoengine.shader.uniform.*;
import org.plutoengine.shader.uniform.auto.AutoViewportProjection;

@ShaderProgram
public final class FontShader extends ShaderBase
{
    @AutoViewportProjection
    @Uniform(name = "projection")
    public UniformMat4 projectionMatrix;

    @Uniform(name = "transformation")
    public UniformMat4 transformationMatrix;

    @Uniform
    public UniformVec2 uvBase;

    @Uniform
    public UniformVec2 uvDelta;

    @Uniform
    public UniformRGBA recolor;

    @Uniform
    public UniformBoolean italic;

    @VertexArrayAttribute(ReservedAttributes.POSITION)
    public int position;

    @VertexArrayAttribute(ReservedAttributes.UV)
    public int uvCoords;
}
