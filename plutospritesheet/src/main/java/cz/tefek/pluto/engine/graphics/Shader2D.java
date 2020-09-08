package cz.tefek.pluto.engine.graphics;

import org.joml.Matrix3x2fc;
import org.joml.Matrix4fc;

import cz.tefek.pluto.engine.graphics.gl.vao.attrib.ReservedAttributes;
import cz.tefek.pluto.engine.shader.ShaderBase;
import cz.tefek.pluto.engine.shader.ShaderProgram;
import cz.tefek.pluto.engine.shader.VertexArrayAttribute;
import cz.tefek.pluto.engine.shader.uniform.Uniform;
import cz.tefek.pluto.engine.shader.uniform.UniformMat3x2;
import cz.tefek.pluto.engine.shader.uniform.UniformMat4;
import cz.tefek.pluto.engine.shader.uniform.UniformVec2;
import cz.tefek.pluto.engine.shader.uniform.UniformVec4;
import cz.tefek.pluto.engine.shader.uniform.auto.AutoViewportProjection;

/**
 * @author 493msi
 *
 */
@ShaderProgram
public final class Shader2D extends ShaderBase implements IShader2D
{
    @AutoViewportProjection
    @Uniform(name = "projection")
    public UniformMat4 projectionMatrix;

    @Uniform(name = "transformation")
    public UniformMat3x2 transformationMatrix;

    @Uniform
    public UniformVec2 uvBase;

    @Uniform
    public UniformVec2 uvDelta;

    @Uniform
    public UniformVec4 recolor;

    @VertexArrayAttribute(ReservedAttributes.POSITION)
    public int position;

    @VertexArrayAttribute(ReservedAttributes.UV)
    public int uvCoords;

    @Override
    public void loadUV(float uvStartX, float uvStartY, float uWidth, float vHeight)
    {
        this.uvBase.load(uvStartX, uvStartY);
        this.uvDelta.load(uWidth, vHeight);
    }

    @Override
    public void loadRecolor(float r, float g, float b, float a)
    {
        this.recolor.load(r, g, b, a);
    }

    @Override
    public void loadProjectionMatrix(Matrix4fc matrix)
    {
        this.projectionMatrix.load(matrix);
    }

    @Override
    public void loadTransformationMatrix(Matrix3x2fc matrix)
    {
        this.transformationMatrix.load(matrix);
    }
}
