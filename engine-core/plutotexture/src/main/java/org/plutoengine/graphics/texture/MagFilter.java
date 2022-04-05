package org.plutoengine.graphics.texture;

import org.lwjgl.opengl.GL33;

import org.plutoengine.gl.IOpenGLEnum;

public enum MagFilter implements IOpenGLEnum
{
    NEAREST(GL33.GL_NEAREST),
    LINEAR(GL33.GL_LINEAR);

    MagFilter(int id)
    {
        this.id = id;
    }

    private final int id;

    @Override
    public int getGLID()
    {
        return this.id;
    }
}
