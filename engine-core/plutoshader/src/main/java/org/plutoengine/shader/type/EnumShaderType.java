package org.plutoengine.shader.type;

import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import org.plutoengine.gl.IOpenGLEnum;

public enum EnumShaderType implements IOpenGLEnum
{
    VERTEX(GL33.GL_VERTEX_SHADER),
    GEOMETRY(GL33.GL_GEOMETRY_SHADER),
    COMPUTE(GL43.GL_COMPUTE_SHADER),
    FRAGMENT(GL33.GL_FRAGMENT_SHADER),
    TESSELATION_CONTROL(GL40.GL_TESS_CONTROL_SHADER),
    TESSELATION_EVALUATION(GL40.GL_TESS_EVALUATION_SHADER);

    private final int id;

    EnumShaderType(int id)
    {
        this.id = id;
    }

    public int getGLID()
    {
        return this.id;
    }
}
