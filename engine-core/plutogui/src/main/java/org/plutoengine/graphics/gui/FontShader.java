package org.plutoengine.graphics.gui;

import org.joml.Matrix3fc;
import org.plutoengine.graphics.gl.vao.attrib.ReservedAttributes;
import org.plutoengine.shader.ShaderBase;
import org.plutoengine.shader.ShaderProgram;
import org.plutoengine.shader.VertexArrayAttribute;
import org.plutoengine.shader.uniform.*;
import org.plutoengine.shader.uniform.auto.AutoViewportProjection;

@ShaderProgram
public final class FontShader extends ShaderBase implements IGUIShader
{
    @AutoViewportProjection
    @Uniform(name = "projection")
    public UniformMat4 projectionMatrix;

    @Uniform(name = "transformation")
    public UniformMat3 transformationMatrix;

    @Uniform
    public UniformRGBA recolor;

    @Uniform
    public UniformBoolean italic;

    @VertexArrayAttribute(ReservedAttributes.POSITION)
    public int position;

    @VertexArrayAttribute(ReservedAttributes.UV)
    public int uvCoords;

    @VertexArrayAttribute(2)
    public int page;

    @Override
    public void setTransform(Matrix3fc transform)
    {
        this.transformationMatrix.load(transform);
    }
}
