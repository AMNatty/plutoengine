package cz.tefek.pluto.engine.graphics.font;

import cz.tefek.pluto.engine.graphics.gl.vao.attrib.ReservedAttributes;
import cz.tefek.pluto.engine.shader.ShaderBase;
import cz.tefek.pluto.engine.shader.ShaderProgram;
import cz.tefek.pluto.engine.shader.VertexArrayAttribute;
import cz.tefek.pluto.engine.shader.uniform.Uniform;
import cz.tefek.pluto.engine.shader.uniform.UniformBoolean;
import cz.tefek.pluto.engine.shader.uniform.UniformMat4;
import cz.tefek.pluto.engine.shader.uniform.UniformVec2;
import cz.tefek.pluto.engine.shader.uniform.UniformVec4;
import cz.tefek.pluto.engine.shader.uniform.auto.AutoViewportProjection;

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
    public UniformVec4 recolor;

    @Uniform
    public UniformBoolean italic;

    @VertexArrayAttribute(ReservedAttributes.POSITION)
    public int position;

    @VertexArrayAttribute(ReservedAttributes.UV)
    public int uvCoords;
}
