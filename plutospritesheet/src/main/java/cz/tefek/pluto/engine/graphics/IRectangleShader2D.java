package cz.tefek.pluto.engine.graphics;

import org.joml.Matrix3x2fc;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector4fc;

import cz.tefek.pluto.engine.shader.IShaderProgram;

public interface IRectangleShader2D extends IShaderProgram
{
    void loadProjectionMatrix(Matrix4fc matrix);

    void loadTransformationMatrix(Matrix3x2fc matrix);

    default void loadUV(Vector2fc uvBase, Vector2fc uvDelta)
    {
        this.loadUV(uvBase.x(), uvBase.y(), uvDelta.x(), uvDelta.y());
    }

    void loadUV(float uBase, float yBase, float uWidth, float vHeight);

    default void loadRecolor(Vector4fc col)
    {
        this.loadUV(col.x(), col.y(), col.z(), col.w());
    }

    void loadRecolor(float r, float g, float b, float a);
}
