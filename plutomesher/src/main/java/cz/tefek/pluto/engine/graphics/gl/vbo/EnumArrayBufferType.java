package cz.tefek.pluto.engine.graphics.gl.vbo;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.gl.IOpenGLEnum;

public enum EnumArrayBufferType implements IOpenGLEnum
{
    FLOAT(GL33.GL_FLOAT),
    INT(GL33.GL_INT),
    UNSIGNED_INT(GL33.GL_UNSIGNED_INT);

    private final int glID;

    EnumArrayBufferType(int glEnum)
    {
        this.glID = glEnum;
    }

    @Override
    public int getGLID()
    {
        return this.glID;
    }
}
