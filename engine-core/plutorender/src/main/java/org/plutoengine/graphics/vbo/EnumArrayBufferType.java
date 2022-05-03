package org.plutoengine.graphics.vbo;

import org.lwjgl.opengl.GL33;

import org.plutoengine.graphics.gl.IOpenGLEnum;

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
